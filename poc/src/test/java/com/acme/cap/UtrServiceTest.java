package com.acme.cap;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.acme.cap.message.CreateSnapshot;
import com.acme.cap.message.RegisterReference;
import com.acme.cap.repository.UtrRepository;

public class UtrServiceTest {

    private UtrService service;

    @Before
    public void createService() {
        service = new UtrService(new UtrRepository() {
            
            @Override
            public void registerTransaction(long utrRegisterId, long transactionId) {
            }
            
            @Override
            public long getOrCreateRegister(String transactionRef) {
                return 0;
            }
        });
    }

    @Test
    public void testRegisterReferenceReturnsExpectedIdAndRef() {
        RegisterReference message = RegisterReference.newMessage("REF_101", 1);

        CreateSnapshot out = service.registerReference(message);
        assertEquals("Same transaction Id", 1, out.getTransactionId());
        assertEquals("transaction reference", "REF_101", out.getTransactionRef());
        // assertNotNull(out.getUtrRegisterId());
    }

    @Test
    public void testFilter() {
        String message = "1, REF_101, A0001, 4999";
        RegisterReference out = service.filter(message);

        assertEquals(1, out.transactionId());
        assertEquals("REF_101", out.transactionRef());
    }
}
