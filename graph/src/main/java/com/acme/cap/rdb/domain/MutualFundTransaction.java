package com.acme.cap.rdb.domain;

// The transactionRef for each transaction is presented differently based on underlying source systems.
// But assuming that they all have a unique id.
public class MutualFundTransaction {
    private final long id;
    private final String mutualFundRef; // FIXME some random ref number
    private final String sonicReferenceId; // FIXME made up ref id
    private final long feesInCents;

    private MutualFundTransaction(long id, String mutualFundRef, String sonicReferenceId,
            long feesInCents) {
        this.id = id;
        this.mutualFundRef = mutualFundRef;
        this.sonicReferenceId = sonicReferenceId;
        this.feesInCents = feesInCents;
    }

    public long getId() {
        return id;
    }

    public String getMutualFundRef() {
        return mutualFundRef;
    }

    public String getSonicReferenceId() {
        return sonicReferenceId;
    }

    public long getFeesInCents() {
        return feesInCents;
    }

    public TransactionRef getRef() {
        return TransactionRef.of(this);
    }

}
