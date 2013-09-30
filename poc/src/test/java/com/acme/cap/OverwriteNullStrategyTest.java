package com.acme.cap;

import static org.junit.Assert.*;

import org.junit.Test;

import com.acme.cap.domain.Custody;
import com.acme.cap.domain.UtrSnapshot;

public class OverwriteNullStrategyTest {

    @Test
    public void testMergeCustodyTransaction() {

        long utrRegisterId = 1L;
        int snapshotVersion = 1;
        Long amount = Long.valueOf(499L);
        String acctNumber = "ACCT-100", currency = "USD";
        UtrSnapshot latest = new UtrSnapshot.Builder(utrRegisterId, snapshotVersion)
                .accountNumber(acctNumber)
                .amount(amount)
                .currency(currency)
                .build();

        long txId = 101L;
        String refId = "REF-A123";
        Long newAmount = Long.valueOf(699L);
        Custody transaction = new Custody.Builder(txId, refId, acctNumber)
                .amount(newAmount)
                .currency("NZD")
                .build();

        OverwriteNullStrategy strategy = new OverwriteNullStrategy();
        UtrSnapshot merged = strategy.merge(latest, transaction);

        assertEquals(utrRegisterId, merged.getUtrRegisterId());
        assertEquals(snapshotVersion + 1, merged.getVersion());
        assertEquals(acctNumber, merged.getAccountNumber());
        // the new amount doesn't overwrite
        assertEquals(amount.longValue(), merged.getAmount().longValue());
        assertEquals(currency, merged.getCurrency());
    }

    @Test
    public void testMergeCustodyTransactionForNulls() {

        long utrRegisterId = 1L;
        int snapshotVersion = 1;
        String acctNumber = "ACCT-100";
        UtrSnapshot latest = new UtrSnapshot.Builder(utrRegisterId, snapshotVersion)
                .accountNumber(acctNumber)
                .build();

        long txId = 101L;
        String refId = "REF-A123";
        Long newAmount = Long.valueOf(699L);
        String currency = "USD";
        Custody transaction = new Custody.Builder(txId, refId, acctNumber)
                .amount(newAmount)
                .currency(currency)
                .build();

        OverwriteNullStrategy strategy = new OverwriteNullStrategy();
        UtrSnapshot merged = strategy.merge(latest, transaction);

        assertEquals(utrRegisterId, merged.getUtrRegisterId());
        assertEquals(snapshotVersion + 1, merged.getVersion());
        assertEquals(acctNumber, merged.getAccountNumber());
        assertEquals(newAmount, merged.getAmount());
        assertEquals(currency, merged.getCurrency());
    }

}
