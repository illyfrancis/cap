package com.acme.cap.rdb.domain;

public class Utr {
    private final long id;
    private final String utrRef;

    private Utr(long utrId, String utrRef) {
        this.id = utrId;
        this.utrRef = utrRef;
    }

    public long getUtrId() {
        return id;
    }
    
    public String getUtrRef() {
        return utrRef;
    }
}
