package com.acme.cap.domain;

import com.google.common.base.Objects;

public class CorporateAction implements Transaction {

    private final long id;
    private final String referenceId;
    private final String accountNumber;
    private Long amount;
    private String currency;
    private Long utrRegisterId;

    private CorporateAction(Builder builder) {
        this.id = builder.id;
        this.referenceId = builder.referenceId;
        this.accountNumber = builder.accountNumber;
        this.amount = builder.amount;
        this.currency = builder.currency;
        this.utrRegisterId = builder.utrRegisterId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.acme.cap.domain.ITransaction#getId()
     */
    @Override
    public long getId() {
        return id;
    }

    /*
     * (non-Javadoc)
     * 
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

        final CorporateAction other = (CorporateAction) obj;
        return this.id == other.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.id, this.referenceId, this.accountNumber,
                this.amount, this.currency, this.utrRegisterId);
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

        public Builder amount(long amount) {
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

        public CorporateAction build() {
            return new CorporateAction(this);
        }
    }

}
