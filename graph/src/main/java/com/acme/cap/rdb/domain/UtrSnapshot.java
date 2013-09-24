package com.acme.cap.rdb.domain;

public class UtrSnapshot {
    private final long utrRegisterId;
    private final long version;
    private final Long cashTransactionId;
    private final Long fxDetailId;
    private final Long mutualFundTransactionId;

    private UtrSnapshot(Builder builder) {
        utrRegisterId = builder.utrRegisterId;
        version = builder.version;
        cashTransactionId = builder.cashTransactionId;
        fxDetailId = builder.fxDetailId;
        mutualFundTransactionId = builder.mutualFundTransactionId;
    }

    public long getUtrRegisterId() {
        return utrRegisterId;
    }

    public long getVersion() {
        return version;
    }

    public long getCashTransactionId() {
        return cashTransactionId;
    }

    public long getFxDetailId() {
        return fxDetailId;
    }

    public long getMutualFundTransactionId() {
        return mutualFundTransactionId;
    }
    
    public static class Builder {
        private final long utrRegisterId;
        private final long version;
        private Long cashTransactionId = null;
        private Long fxDetailId = null;
        private Long mutualFundTransactionId = null;
        
        public Builder(long utrRegisterId, long version) {
            this.utrRegisterId = utrRegisterId;
            this.version = version;
        }
        
        public Builder cashTransactionId(long id) {
            this.cashTransactionId = Long.valueOf(id);
            return this;
        }

        public Builder fxDetailId(long id) {
            this.fxDetailId = Long.valueOf(id);
            return this;
        }
        
        public Builder mutualFundTransactionId(long id) {
            this.mutualFundTransactionId = Long.valueOf(id);
            return this;
        }
        
        public UtrSnapshot build() {
            return new UtrSnapshot(this);
        }
    }

}
