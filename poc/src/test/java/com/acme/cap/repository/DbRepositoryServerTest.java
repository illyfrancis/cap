package com.acme.cap.repository;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.acme.cap.domain.Custody;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:META-INF/spring/serverdb.xml")
public class DbRepositoryServerTest {

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
        
        repository.save(cash);
        
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
        repository.save(cash);
        
        // save utr_register first
        long utrRegisterId = repository.getOrCreateRegister("REF-001");
        
        // update
        repository.registerTransaction(utrRegisterId, transactionId);

        JdbcTemplate jdbcTemplate = new JdbcTemplate(this.dataSource);
        String query = "select utr_register_id from cash_transaction where id = ?";
        long utrRegisterIdFromDb = jdbcTemplate.queryForLong(query, transactionId);
        assertEquals(utrRegisterId, utrRegisterIdFromDb);

    }
}
