package com.acme.cap.rdb.domain;

import javax.transaction.Transaction;

import com.acme.cap.rdb.domain.mock.MockTransaction;

public class UtrRepository implements Repository {

    // putIfAbsent semantic, must be within db transaction
    /* (non-Javadoc)
     * @see com.acme.cap.rdb.domain.Repository#createOrGetUtrRegister(java.lang.String, long)
     */
    @Override
    public long createOrGetUtrRegister(TransactionRef transactionRef, long transactionId) throws Exception {
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

    @Override
    public void setUtrRegisterIdOn(long transactionId, long utrRegisterId) {
        // TODO Auto-generated method stub
        
    }
}
