package com.acme.cap.domain;

import com.google.common.base.Objects;

public class Custody implements Transaction {
    private final long id;
    private final String referenceId;
    private final String accountNumber;
    private Long amount;
    private String currency;
    private Long utrRegisterId;

    public static Custody of(long id, String referenceId, String accontNumber, Long amount) {
        return new Builder(id, referenceId, accontNumber).amount(amount).build();
    }

    private Custody(Builder builder) {
        this.id = builder.id;
        this.referenceId = builder.referenceId;
        this.accountNumber = builder.accountNumber;
        this.amount = builder.amount;
        this.currency = builder.currency;
        this.utrRegisterId = builder.utrRegisterId;
    }

    /* (non-Javadoc)
     * @see com.acme.cap.domain.ITransaction#getId()
     */
    @Override
    public long getId() {
        return id;
    }

    /* (non-Javadoc)
     * @see com.acme.cap.domain.ITransaction#getReferenceId()
     */
    @Override
    public String getReferenceId() {
        return referenceId;
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

    public Long getUtrRegisterId() {
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

        final Custody other = (Custody) obj;
        return this.id == other.id;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", id)
                .add("referenceId", referenceId)
                .add("accountNumber", accountNumber)
                .add("amount", amount)
                .add("currency", currency)
                .add("utrRegisterId", utrRegisterId)
                .toString();
    }

    public static class Builder {
        private final long id;
        private final String referenceId;
        private final String accountNumber;
        private Long amount = 0L;
        private String currency = null;
        private Long utrRegisterId = null;

        public Builder(long id, String referenceId, String accontNumber) {
            this.id = id;
            this.referenceId = referenceId;
            this.accountNumber = accontNumber;
        }

        public Builder amount(Long amount) {
            this.amount = amount;
            return this;
        }

        public Builder currency(String currency) {
            this.currency = currency;
            return this;
        }

        public Builder utrRegisterId(Long utrRegisterId) {
            this.utrRegisterId = utrRegisterId;
            return this;
        }

        public Custody build() {
            return new Custody(this);
        }
    }
}
