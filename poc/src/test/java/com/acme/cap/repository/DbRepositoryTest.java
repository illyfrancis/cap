package com.acme.cap.repository;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:META-INF/spring/embedded.xml")
public class DbRepositoryTest {

    /*
     * @Autowired private ApplicationContext context;
     */
    @Inject
    // same as @Autowired but more 'standard'
    private DbRepository repository;

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
        try {
            repository.addUtrRegister("R001");
        } catch (Throwable e) {
            assertTrue("first insert should be ok", false);
        }

        // but this should throw DuplicateKeyException
        repository.addUtrRegister("R001");
    }
}
