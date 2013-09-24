package com.acme.cap.rdb.domain;

public class TransactionRef {
    private final String ref;

    public static TransactionRef of(CashTransaction transaction) {
        return new TransactionRef(transaction.getSonicReferenceId());
    }

    public static TransactionRef of(MutualFundTransaction mutualFundTransaction) {
        return new TransactionRef(mutualFundTransaction.getSonicReferenceId());
    }

    private TransactionRef(String ref) {
        this.ref = ref;
    }

    public String getRef() {
        return ref;
    }
}
