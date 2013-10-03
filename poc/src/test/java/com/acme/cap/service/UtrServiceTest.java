package com.acme.cap.service;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.DuplicateKeyException;

import com.acme.cap.domain.Custody;
import com.acme.cap.domain.Transaction;
import com.acme.cap.domain.UtrSnapshot;
import com.acme.cap.message.CreateSnapshot;
import com.acme.cap.message.GenerateUtr;
import com.acme.cap.message.RegisterReference;
import com.acme.cap.repository.UtrRepository;
import com.acme.cap.service.merge.UtrMergeStrategy;

public class UtrServiceTest {

    UtrService service;
    UtrRepository repository = mock(UtrRepository.class);
    UtrMergeStrategy merger = mock(UtrMergeStrategy.class);
    
    @Before
    public void createService() {
        service = new UtrService(repository, merger);
    }

    @Test
    public void testFilter() {
        String message = "1, REF_101, A0001, 4999";

        RegisterReference out = service.filter(message);

        verify(repository, times(1)).saveTransaction((Custody) anyObject());
        assertThat("REF_101", equalTo(out.transactionRef()));
        assertThat(out.transaction(), instanceOf(Custody.class));
    }

    @Test
    public void testRegisterReferenceReturnsExpectedIdAndRef() {
        long transactinoId = 102L;
        String transactionRef = "REF_101";
        String accountNumber = "A-001";
        long amount = 299L;
        long utrRegisterId = 456L;

        Transaction cash = Custody.of(transactinoId, transactionRef, accountNumber, amount);
        RegisterReference message = RegisterReference.newMessage(transactionRef, cash);
        when(repository.getOrCreateRegister(transactionRef)).thenReturn(utrRegisterId);

        CreateSnapshot out = service.registerReference(message);

        verify(repository, times(1)).getOrCreateRegister(transactionRef);
        verify(repository, times(1)).registerTransaction(utrRegisterId, transactinoId);
        assertThat("Same transaction Id", out.transaction().getId(), is(transactinoId));
        assertThat("Same cash transaction", out.transaction(), equalTo(cash));
        assertThat("transaction reference", out.getTransactionRef(), is(transactionRef));
    }

    @Test
    // (expected = DuplicateKeyException.class)
    public void testRegisterReferenceThrowsDuplicateKeyException() {
        long transactinoId = 102L;
        String transactionRef = "REF_101";
        String accountNumber = "A-001";
        long amount = 299L;
        String errorMessage = "duplicate";

        Transaction cash = Custody.of(transactinoId, transactionRef, accountNumber, amount);
        RegisterReference message = RegisterReference.newMessage(transactionRef, cash);
        when(repository.getOrCreateRegister(transactionRef)).thenThrow(
                new DuplicateKeyException(errorMessage));

        try {
            service.registerReference(message);
            Assert.fail("this shouldn't happen");
        } catch (DuplicateKeyException e) {
            assertThat("expect DuplicateKeyException", true);
        }

        verify(repository, never()).registerTransaction(anyLong(), anyLong());
    }

    @Test
    public void testAddSnapshot() {
        long transactinoId = 102L;
        String transactionRef = "REF_101";
        String accountNumber = "A-001";
        long utrRegisterId = 456L;

        Transaction transaction = new Custody.Builder(transactinoId, transactionRef, accountNumber).build();
        CreateSnapshot message = CreateSnapshot.newMessage(transactionRef, transaction, utrRegisterId);

        // stubbing...
        UtrSnapshot snapshot = new UtrSnapshot.Builder(utrRegisterId, 1).build();
        UtrSnapshot merged = new UtrSnapshot.Builder(utrRegisterId, 1).accountNumber(accountNumber).build();
        when(repository.getLatestSnapshot(utrRegisterId)).thenReturn(snapshot);
        when(merger.merge(snapshot, transaction)).thenReturn(merged);

        GenerateUtr out = service.addSnapshot(message);

        verify(repository).getLatestSnapshot(utrRegisterId);
        verify(repository).saveSnapshot(merged);
        assertThat("same transaction reference", out.getTransactionRef(), is(transactionRef));
        assertThat("same merged utr snapshot", out.getUtrSnapshot(), is(merged));
        assertThat("same merged utr snapshot", out.getUtrSnapshot(), sameInstance(merged));
    }
    
}
