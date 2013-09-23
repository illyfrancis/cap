package com.acme.cap.rdb.domain;

// The transactionRef for each transaction is presented differently based on underlying source systems.
// But assuming that they all have a unique id.
public class CashTransaction {

    private final long id;
    private final String sonicReferenceId;
    private final String actionWorldReferenceId;
    
    private CashTransaction(long id, String sonicReferenceId, String actionWorldReferenceId) {
        this.id = id;
        this.actionWorldReferenceId = actionWorldReferenceId;
        this.sonicReferenceId = sonicReferenceId;
    }
    
    public long getId() {
        return id;
    }

    public TransactionRef getRef() {
        // TODO Auto-generated method stub
        return TransactionRef.of(this);
    }

    public String getActionWorldReferenceId() {
        return actionWorldReferenceId;
    }
    
    public String getSonicReferenceId() {
        return sonicReferenceId;
    }
}
