package com.acme.cap.message;

import java.util.Objects;

public class CreateSnapshot {
    private final String transactionRef;
    private final long transactionId;
    private final long utrRegisterId;

    public static CreateSnapshot newMessage(String transactionRef, long transactionId, long utrRegisterId) {
        return new CreateSnapshot(transactionRef, transactionId, utrRegisterId);
    }

    private CreateSnapshot(String transactionRef, long transactionId, long utrRegisterId) {
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

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        final CreateSnapshot other = (CreateSnapshot) obj;
        return Objects.equals(this.transactionRef, other.transactionRef)
                && Objects.equals(this.transactionId, other.transactionId)
                && Objects.equals(this.utrRegisterId, other.utrRegisterId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.transactionId, this.transactionRef, this.utrRegisterId);
    }

    @Override
    public String toString() {
        return com.google.common.base.Objects.toStringHelper(this)
                .addValue(transactionRef)
                .addValue(transactionId)
                .addValue(utrRegisterId)
                .toString();
    }
}