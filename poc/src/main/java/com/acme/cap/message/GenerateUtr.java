package com.acme.cap.message;

import com.acme.cap.domain.UtrSnapshot;
import com.google.common.base.Objects;

public class GenerateUtr {
    private final String transactionRef;
    private final UtrSnapshot utrSnapshot;

    public static GenerateUtr newMessage(String transactionRef, UtrSnapshot utrSnapshot) {
        return new GenerateUtr(transactionRef, utrSnapshot);
    }

    private GenerateUtr(String transactionRef, UtrSnapshot utrSnapshot) {
        this.transactionRef = transactionRef;
        this.utrSnapshot = utrSnapshot;
    }

    public String getTransactionRef() {
        return transactionRef;
    }

    public UtrSnapshot getUtrSnapshot() {
        return utrSnapshot;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("transactionRef", transactionRef)
                .addValue(utrSnapshot)
                .toString();
    }
}
