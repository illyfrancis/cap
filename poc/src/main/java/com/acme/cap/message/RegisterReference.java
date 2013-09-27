package com.acme.cap.message;

import com.google.common.base.Objects;

public class RegisterReference {

    private final String transactionRef;
    private final long transactionId;

    public static RegisterReference newMessage(String transactionRef, long transactionId) {
        return new RegisterReference(transactionRef, transactionId);
    }

    private RegisterReference(String transactionRef, long transactionId) {
        this.transactionRef = transactionRef;
        this.transactionId = transactionId;
    }

    public long transactionId() {
        return transactionId;
    }

    public String transactionRef() {
        return transactionRef;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("txRef", transactionRef)
                .add("txId", transactionId)
                .toString();
    }
}
