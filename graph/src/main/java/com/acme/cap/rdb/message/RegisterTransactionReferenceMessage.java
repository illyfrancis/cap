package com.acme.cap.rdb.message;

import com.acme.cap.rdb.domain.CashTransaction;
import com.acme.cap.rdb.domain.MutualFundTransaction;
import com.acme.cap.rdb.domain.TransactionRef;

public class RegisterTransactionReferenceMessage {
    private final String transactionRef;
    private final long transactionId;
    
    public static RegisterTransactionReferenceMessage of(CashTransaction transaction) {
        return new RegisterTransactionReferenceMessage(transaction.getRef(), transaction.getId());
    }
    
    public static RegisterTransactionReferenceMessage of(MutualFundTransaction transaction) {
        return new RegisterTransactionReferenceMessage(transaction.getRef(), transaction.getId());
    }
    
    private RegisterTransactionReferenceMessage(TransactionRef transactionRef, long transactionId) {
        this.transactionRef = transactionRef.getRef();
        this.transactionId = transactionId;
    }

    public long getTransactionId() {
        return transactionId;
    }
    
    public String getTransactionRef() {
        return transactionRef;
    }
    
}
