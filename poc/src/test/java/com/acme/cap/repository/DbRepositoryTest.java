package com.acme.cap.repository;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.acme.cap.domain.Custody;
import com.acme.cap.domain.UtrSnapshot;
import com.acme.cap.repository.DbRepository.UtrSnapshotRowMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:META-INF/spring/serverdb.xml")
public class DbRepositoryTest {
    
    Logger log = LoggerFactory.getLogger(getClass());

    /*
     * @Autowired private ApplicationContext context;
     */
    @Inject
    // same as @Autowired but more 'standard'
    private DbRepository repository;

    @Inject
    private DataSource dataSource;

    @Test
    public void testGetOrCreateRegister() {
        String transactionRef = "ref-123-";
        long utrRegisterId = repository.getOrCreateRegister(transactionRef);
        utrRegisterId = repository.getOrCreateRegister(transactionRef);
        assertThat("UtrRegisterId cannot be zero", utrRegisterId, greaterThan(0L));
    }

    @Test
    public void testFindExpectedUtrRegister() {
        String transactionRef = "ref-123";
        long utrRegisterId = repository.getOrCreateRegister(transactionRef);
        utrRegisterId = repository.getOrCreateRegister(transactionRef);
        assertEquals(100L, utrRegisterId);
    }

    @Test(expected = DuplicateKeyException.class)
    public void testAddUtrRegister() {
        // FIXME - it will clash, sooner or later
        String ref = "R-" + String.valueOf(System.currentTimeMillis()).substring(6);
        try {
            repository.addUtrRegister(ref);
        } catch (Throwable e) {
            assertTrue("first insert should pass", false);
        }

        // but this should throw DuplicateKeyException
        repository.addUtrRegister(ref);
    }

    @Test
    public void testSaveTransaction() {
        long txId = System.currentTimeMillis();
        Custody cash = new Custody.Builder(txId, "REF-SON-1", "ACC-0001-B")
                .amount(999L).build();

        repository.saveTransaction(cash);

        JdbcTemplate jdbcTemplate = new JdbcTemplate(this.dataSource);
        String query = "select count(*) from cash_transaction where id = ?";
        int count = jdbcTemplate.queryForInt(query, txId);
        assertEquals(1, count);
    }

    @Test
    public void testRegisterTransaction() {
        long transactionId = System.currentTimeMillis();
        Custody cash = new Custody.Builder(transactionId, "REF-SON-1", "ACC-0001-B")
                .amount(999L).build();
        repository.saveTransaction(cash);

        // save utr_register first
        long utrRegisterId = repository.getOrCreateRegister("REF-001");

        // update
        repository.registerTransaction(utrRegisterId, transactionId);

        JdbcTemplate jdbcTemplate = new JdbcTemplate(this.dataSource);
        String query = "select utr_register_id from cash_transaction where id = ?";
        long utrRegisterIdFromDb = jdbcTemplate.queryForLong(query, transactionId);
        assertEquals(utrRegisterId, utrRegisterIdFromDb);
    }

    @Test
    public void testSaveUtrSnapshot() {
        long utrRegisterId = System.currentTimeMillis();
        int version = 3;
        UtrSnapshot utrSnapshot = new UtrSnapshot.Builder(utrRegisterId, version)
                .accountNumber("ACCT-1").build();
        repository.saveSnapshot(utrSnapshot);

        JdbcTemplate jdbcTemplate = new JdbcTemplate(this.dataSource);
        String query = "select version from utr_snapshot where utr_register_id = ?";
        int versionFromDb = jdbcTemplate.queryForInt(query, utrRegisterId);
        assertEquals(version, versionFromDb);
    }

    @Test
    public void testSaveUtrSnapshotWithAllFieldsSet() {
        long utrRegisterId = System.currentTimeMillis();
        int version = 1;
        Long amount = Long.valueOf(99L);
        String currency = "GBP";
        UtrSnapshot utrSnapshot = new UtrSnapshot.Builder(utrRegisterId, version)
                .accountNumber("ACCT-1")
                .amount(amount)
                .currency(currency)
                .build();
        repository.saveSnapshot(utrSnapshot);

        JdbcTemplate jdbcTemplate = new JdbcTemplate(this.dataSource);
        String query = "select * from utr_snapshot where utr_register_id = ?";

        UtrSnapshotRowMapper rowMapper = new UtrSnapshotRowMapper();
        UtrSnapshot fromDb = jdbcTemplate.queryForObject(query, rowMapper, utrRegisterId);

        assertEquals(version, fromDb.getVersion());
    }

    @Test
    public void testGetLatestSnapshot() {
        long utrRegisterId = System.currentTimeMillis();
        String account = "acct-001";

        try {
            // version 1
            repository.saveSnapshot(new UtrSnapshot.Builder(utrRegisterId, 1)
                    .accountNumber(account).build());

            // version 9
            repository.saveSnapshot(new UtrSnapshot.Builder(utrRegisterId, 9)
                    .accountNumber(account).build());

            // version 2
            repository.saveSnapshot(new UtrSnapshot.Builder(utrRegisterId, 2)
                    .accountNumber(account).build());
        } catch (Exception e) {
            log.info("What's the exception?", e);
        }

        UtrSnapshot snapshot = repository.getLatestSnapshot(utrRegisterId);
        assertEquals(9, snapshot.getVersion());
        assertEquals(account, snapshot.getAccountNumber());
    }

    @Test
    public void testGetLatestSnapShotReturnsNull() {
        long utrRegisterId = -1;
        UtrSnapshot snapshot = repository.getLatestSnapshot(utrRegisterId);
        assertNull(snapshot);
    }
}
