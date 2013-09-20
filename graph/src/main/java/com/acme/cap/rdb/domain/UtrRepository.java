package com.acme.cap.rdb.domain;

import javax.transaction.Transaction;

import com.acme.cap.rdb.domain.mock.MockTransaction;

public class UtrRepository {

    // putIfAbsent semantic, must be within db transaction
    public long createOrGetUtrRegister(String transactionRef, long transactionId) throws Exception {
        long utrRegisterId = 0L;
        Transaction tx = new MockTransaction();
        try {
            UtrRegister utrRegister = UtrRegister.build();
            // select * from UtrRegister
            // where transactionRef = {transactionRef};

            boolean isNewTransactionRef = utrRegister == null;
            if (isNewTransactionRef) {
                // insert into UtrRegister values {...}
            }

            utrRegisterId = utrRegister.getId();
            // update Transaction with the "utrRegisterId" where id =
            // {transactionId}

            tx.commit();
        } catch (Exception ex) {
            tx.rollback();
        }
        return utrRegisterId;
    }
}
