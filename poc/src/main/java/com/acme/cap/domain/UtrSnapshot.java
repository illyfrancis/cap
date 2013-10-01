package com.acme.cap.domain;

import com.google.common.base.Charsets;
import com.google.common.base.Objects;
import com.google.common.hash.Funnel;
import com.google.common.hash.PrimitiveSink;

public class UtrSnapshot {

    private final long utrRegisterId;
    private final int version;
    private String accountNumber;
    private Long amount;
    private String currency;

    public static UtrSnapshot newVersion(long utrRegisterId) {
        return new Builder(utrRegisterId, 0).build();
    }

    private UtrSnapshot(Builder builder) {
        this.utrRegisterId = builder.utrRegisterId;
        this.version = builder.version;
        this.accountNumber = builder.accountNumber;
        this.amount = builder.amount;
        this.currency = builder.currency;
    }

    public int getVersion() {
        return version;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public Long getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
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

        final UtrSnapshot other = (UtrSnapshot) obj;
        return this.utrRegisterId == other.utrRegisterId
                && this.version == other.version;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("utrRegisterId", utrRegisterId)
                .add("version", version)
                .add("accountNumber", accountNumber)
                .add("amount", amount)
                .add("currency", currency)
                .toString();
    }

    public static class Builder {

        private final long utrRegisterId;
        private final int version;
        private String accountNumber;
        private Long amount;
        private String currency;

        public Builder(long utrRegisterId, int version) {
            this.utrRegisterId = utrRegisterId;
            this.version = version;
        }

        public Builder accountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
            return this;
        }

        public Builder amount(Long amount) {
            this.amount = amount;
            return this;
        }

        public Builder currency(String currency) {
            this.currency = currency;
            return this;
        }

        public UtrSnapshot build() {
            return new UtrSnapshot(this);
        }
    }

    public static class UtrSnapshotFunnel implements Funnel<UtrSnapshot> {

        private static final long serialVersionUID = -5038983879898300139L;

        @Override
        public void funnel(UtrSnapshot from, PrimitiveSink into) {
            into.putString(from.accountNumber, Charsets.UTF_8)
                    .putLong(from.amount)
                    .putString(from.currency, Charsets.UTF_8);
        }
    }

}
