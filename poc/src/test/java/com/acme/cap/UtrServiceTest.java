package com.acme.cap;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.acme.cap.domain.Transaction;
import com.acme.cap.domain.Custody;
import com.acme.cap.domain.UtrSnapshot;
import com.acme.cap.message.CreateSnapshot;
import com.acme.cap.message.RegisterReference;
import com.acme.cap.repository.UtrRepository;

public class UtrServiceTest {

    private UtrService service;

    @Before
    public void createService() {
        service = new UtrService(new UtrRepository() {
            
            @Override
            public void addSnapshot(UtrSnapshot merged) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void save(Custody transaction) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void registerTransaction(long utrRegisterId, long transactionId) {
            }
            
            @Override
            public long getOrCreateRegister(String transactionRef) {
                return 0;
            }
            
            @Override
            public UtrSnapshot latestSnapshot(long utrRegisterId) {
                // TODO Auto-generated method stub
                return null;
            }
        });
    }

    @Test
    public void testRegisterReferenceReturnsExpectedIdAndRef() {
        long txId = 102L;
        Transaction cash = Custody.of(txId, "REF_101", "A-001", 299L);
        RegisterReference message = RegisterReference.newMessage("REF_101", cash);

        CreateSnapshot out = service.registerReference(message);
        assertEquals("Same transaction Id", txId, out.transaction().getId());
        assertEquals("transaction reference", "REF_101", out.getTransactionRef());
        // assertNotNull(out.getUtrRegisterId());
    }

    @Test
    public void testFilter() {
        String message = "1, REF_101, A0001, 4999";
        RegisterReference out = service.filter(message);

        assertEquals(1, out.transaction().getId());
        assertEquals("REF_101", out.transactionRef());
    }
}
