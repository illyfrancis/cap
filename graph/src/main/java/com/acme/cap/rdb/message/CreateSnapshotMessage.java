package com.acme.cap.rdb.message;

public class CreateSnapshotMessage {
    private final String transactionRef;
    private final long transactionId;
    private final long utrRegisterId;
    
    public static CreateSnapshotMessage from(String transactionRef, long transactionId, long utrRegisterId) {
        return new CreateSnapshotMessage(transactionRef, transactionId, utrRegisterId);
    }
    
    private CreateSnapshotMessage(String transactionRef, long transactionId, long utrRegisterId) {
        this.transactionRef = transactionRef;
        this.transactionId = transactionId;
        this.utrRegisterId = utrRegisterId;
    }

    public String getTransactionRef() {
        return transactionRef;
    }

    public long getTransactionId() {
        return transactionId;
    }

    public long getUtrRegisterId() {
        return utrRegisterId;
    }
}
