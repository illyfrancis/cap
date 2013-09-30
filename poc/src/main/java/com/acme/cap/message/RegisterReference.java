package com.acme.cap.message;

import com.acme.cap.domain.Transaction;
import com.google.common.base.Objects;

public class RegisterReference {

    private final String transactionRef;
    private final Transaction transaction;

    public static RegisterReference newMessage(String transactionRef, Transaction transaction) {
        return new RegisterReference(transactionRef, transaction);
    }

    private RegisterReference(String transactionRef, Transaction transaction) {
        this.transactionRef = transactionRef;
        this.transaction = transaction;
    }

    public Transaction transaction() {
        return transaction;
    }

    public String transactionRef() {
        return transactionRef;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("transactionRef", transactionRef)
                .add("transaction", transaction)
                .toString();
    }
}
