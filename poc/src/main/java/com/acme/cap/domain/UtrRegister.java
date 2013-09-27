package com.acme.cap.domain;

import com.google.common.base.Objects;

public class UtrRegister {
    private final long id;
    private final String transactionRef;

    public static UtrRegister build(long id, String transactionRef) {
        return new UtrRegister(id, transactionRef);
    }

    private UtrRegister(long id, String transactionRef) {
        this.id = id;
        this.transactionRef = transactionRef;
    }

    public long getId() {
        return id;
    }

    public String getTransactionRef() {
        return transactionRef;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", id)
                .add("ref", transactionRef)
                .toString();
    }
}
