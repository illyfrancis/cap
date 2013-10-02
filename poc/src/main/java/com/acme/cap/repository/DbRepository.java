package com.acme.cap.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import com.acme.cap.domain.Custody;
import com.acme.cap.domain.UtrMessage;
import com.acme.cap.domain.UtrRegister;
import com.acme.cap.domain.UtrSnapshot;
import com.google.common.base.Optional;
import com.google.common.collect.Maps;

public class DbRepository implements UtrRepository {

    private static final Logger log = LoggerFactory.getLogger(DbRepository.class);

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert insertUtrRegister;
    private SimpleJdbcInsert insertTransaction;

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.insertUtrRegister = new SimpleJdbcInsert(dataSource)
                .withTableName("UTR_REGISTER")
                .usingGeneratedKeyColumns("id");
        this.insertTransaction = new SimpleJdbcInsert(dataSource).withTableName("CASH_TRANSACTION");
    }

    @Override
    public void saveTransaction(Custody custody) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", custody.getId());
        params.put("sonic_ref", custody.getReferenceId());
        params.put("account", custody.getAccountNumber());
        params.put("amount", custody.getAmount());
        params.put("currency", custody.getCurrency());
        params.put("utr_register_id", custody.getUtrRegisterId());
        insertTransaction.execute(params);
    }

    @Override
    public long getOrCreateRegister(String transactionRef) throws DuplicateKeyException {
        log.info("getOrCreateRegister with ref [{}]", transactionRef);

        Optional<Long> utrRegisterId = findUtrRegisterId(transactionRef);
        if (utrRegisterId.isPresent()) {
            log.info("found UtrRegister with id [{}]", utrRegisterId.get());
        } else {
            // none found, create one.
            utrRegisterId = Optional.of(addUtrRegister(transactionRef));
            log.info("inserted UtrRegister with id [{}]", utrRegisterId.get());
        }

        // 1. first look for UtrRegister with transactionRef
        // 2. if not found, create one
        // 3. if duplicate exception, retry by throwing another exception
        // but for other exceptions, just throw it
        // 4. if all good return the UtrRegister.id
        return utrRegisterId.get();
    }

    protected Optional<Long> findUtrRegisterId(String transactionRef) {
        String sql = "select id from utr_register where ref = ?";
        Optional<Long> utrRegisterId;
        try {
            utrRegisterId = Optional.of(this.jdbcTemplate.queryForLong(sql, transactionRef));
        } catch (IncorrectResultSizeDataAccessException e) {
            // none found and don't want this exception to propagate any
            // further.
            utrRegisterId = Optional.absent();
        }

        return utrRegisterId;
    }

    protected long addUtrRegister(String transactionRef) throws DuplicateKeyException {
        // "insert into UTR_REGISTER (REF) values (?)";
        Map<String, Object> param = Maps.newHashMap();
        param.put("ref", transactionRef);
        Number newId = insertUtrRegister.executeAndReturnKey(param);
        return newId.longValue();
    }

    public long _getOrCreateRegister(String transactionRef) {
        log.info("getOrCreateRegister with ref [{}]", transactionRef);

        String sql = "select id, ref from utr_register where ref = ?";

        RowMapper<UtrRegister> rowMapper = new RowMapper<UtrRegister>() {
            @Override
            public UtrRegister mapRow(ResultSet rs, int rowNum) throws SQLException {
                UtrRegister utrRegister = UtrRegister.build(rs.getLong("id"), rs.getString("ref"));
                return utrRegister;
            }
        };

        UtrRegister utrRegister = this.jdbcTemplate.queryForObject(sql, rowMapper, transactionRef);

        log.info("Found UtrRegister [{}]", utrRegister);

        // 1. first look for UtrRegister with transactionRef
        // 2. if not found, create one
        // 3. if duplicate exception, retry by throwing another exception
        // but for other exceptions, just throw it
        // 4. if all good return the UtrRegister.id
        return 0;
    }

    @Override
    public void registerTransaction(long utrRegisterId, long transactionId) {
        log.info("registerTransaction with utrRegisterId({}) and tx id ({})", utrRegisterId, transactionId);

        this.jdbcTemplate.update("update cash_transaction set utr_register_id = ? where id = ?",
                utrRegisterId, transactionId);
    }

    @Override
    public UtrSnapshot getLatestSnapshot(long utrRegisterId) {

        // FIXME - rewrite
        String query = "select * from utr_snapshot where utr_register_id = ? and " +
                "version = (select max(version) from utr_snapshot where utr_register_id = ?)";

        UtrSnapshot snapshot = null;
        try {
            UtrSnapshotRowMapper rowMapper = new UtrSnapshotRowMapper();
            snapshot = this.jdbcTemplate.queryForObject(query, rowMapper, utrRegisterId,
                    utrRegisterId);
        } catch (EmptyResultDataAccessException e) {
            snapshot = UtrSnapshot.newVersion(utrRegisterId);
        }
        return snapshot;
    }

    @Override
    public void saveSnapshot(UtrSnapshot utrSnapshot) {
        int row = this.jdbcTemplate.update("insert into utr_snapshot values (?, ?, ?, ?, ?)",
                utrSnapshot.getUtrRegisterId(),
                utrSnapshot.getVersion(),
                utrSnapshot.getAccountNumber(),
                utrSnapshot.getAmount(),
                utrSnapshot.getCurrency());

        log.info("> inserted [{}] row", row);
    }

    static class UtrSnapshotRowMapper implements RowMapper<UtrSnapshot> {
        @Override
        public UtrSnapshot mapRow(ResultSet rs, int rowNum) throws SQLException {
            UtrSnapshot snapshot = new UtrSnapshot.Builder(
                    rs.getLong("utr_register_id"), rs.getInt("version"))
                    .accountNumber(rs.getString("account"))
                    .amount(rs.getLong("amount"))
                    .currency(rs.getString("currency"))
                    .build();
            return snapshot;
        }
    }

    @Override
    public UtrMessage getLastestUtrMessage(long utrRegisterId) {
        // TODO Auto-generated method stub
        return null;
    }
}
