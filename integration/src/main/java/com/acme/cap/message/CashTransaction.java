package com.acme.cap.message;

import com.google.common.base.Objects;


public class CashTransaction {
    private final String transactionId;
    private final String accountNumber;
    private final String status;

    private CashTransaction(String transactionId, String accountNumber, String status) {
        this.transactionId = transactionId;
        this.accountNumber = accountNumber;
        this.status = status;
    }

    public static CashTransaction of(String transactionId, String accountNumber, String status) {
        return new CashTransaction(transactionId, accountNumber, status);
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getStatus() {
        return status;
    }
    
    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("tx", transactionId)
                .add("acct", accountNumber)
                .add("status", status)
                .toString();
    }
}
