package com.acme.cap.rdb.domain;

import javax.transaction.Transaction;

import com.acme.cap.rdb.domain.mock.MockTransaction;

public class UtrRepository implements Repository {

    // putIfAbsent semantic, must be within db transaction
    /*
     * (non-Javadoc)
     * 
     * @see
     * com.acme.cap.rdb.domain.Repository#createOrGetUtrRegister(java.lang.String
     * , long)
     */
    @Override
    public long createOrGetUtrRegister(String transactionRef, long transactionId)
            throws Exception {
        long utrRegisterId = 0L;
        Transaction tx = new MockTransaction();
        try {
            // look for UtrRegister with transactionRef
            // select * from UtrRegister
            // where transactionRef = {transactionRef};
            UtrRegister utrRegister = UtrRegister.build();

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
    public void setUtrRegisterIdOnTransaction(long transactionId, long utrRegisterId) {
        // update transaction with utrRegisterId matching transactionId
        // FIXME need to know about different Transaction types, because each
        // transactionId may refer to different 'transaction' records.
        // update transaction set utr_register_id = {utrRegisterId}
    }
}
