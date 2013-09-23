package com.acme.cap.rdb.message;

import com.acme.cap.rdb.domain.CashTransaction;
import com.acme.cap.rdb.domain.TransactionRef;

public class CoreMessage {
    private final TransactionRef transactionRef;
    private final long transactionId;
    
    public static CoreMessage of(CashTransaction transaction) {
        return new CoreMessage(transaction.getRef(), transaction.getId());
    }
    
    private CoreMessage(TransactionRef transactionRef, long transactionId) {
        this.transactionRef = transactionRef;
        this.transactionId = transactionId;
    }

    public long getTransactionId() {
        return transactionId;
    }
    
    public TransactionRef getTransactionRef() {
        return transactionRef;
    }
}
