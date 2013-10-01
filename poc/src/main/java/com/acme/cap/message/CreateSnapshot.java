package com.acme.cap.message;

import java.util.Objects;

import com.acme.cap.domain.Transaction;

public class CreateSnapshot {
    private final String transactionRef;
    private final Transaction transaction;
    private final long utrRegisterId;

    public static CreateSnapshot newMessage(String transactionRef, Transaction transaction, long utrRegisterId) {
        return new CreateSnapshot(transactionRef, transaction, utrRegisterId);
    }

    private CreateSnapshot(String transactionRef, Transaction transaction, long utrRegisterId) {
        // TODO - copy transaction
        this.transactionRef = transactionRef;
        this.transaction = transaction;
        this.utrRegisterId = utrRegisterId;
    }

    public String getTransactionRef() {
        return transactionRef;
    }

    public Transaction transaction() {
        return transaction;
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
                && Objects.equals(this.transaction, other.transaction)
                && Objects.equals(this.utrRegisterId, other.utrRegisterId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.transaction, this.transactionRef, this.utrRegisterId);
    }

    @Override
    public String toString() {
        return com.google.common.base.Objects.toStringHelper(this)
                .addValue(transactionRef)
                .addValue(transaction)
                .addValue(utrRegisterId)
                .toString();
    }
}