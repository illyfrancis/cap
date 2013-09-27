package com.acme.cap.domain;

import com.google.common.base.Objects;

public class Transaction {

    private final long id;
    private final String referenceId;
    private final String accountNumber;
    private final long amount;

    public static Transaction of(long id, String referenceId, String accontNumber, long amount) {
        return new Transaction(id, referenceId, accontNumber, amount);
    }

    private Transaction(long id, String referenceId, String accontNumber, long amount) {
        this.id = id;
        this.referenceId = referenceId;
        this.accountNumber = accontNumber;
        this.amount = amount;
    }

    public long getId() {
        return id;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public long getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", id)
                .add("referenceId", referenceId)
                .add("accountNumber", accountNumber)
                .add("amount", amount)
                .toString();
    }
}
