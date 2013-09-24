package com.acme.cap.rdb.domain;

// The transactionRef for each transaction is presented differently based on underlying source systems.
// But assuming that they all have a unique id.
public class FxDetail {
    private final long id;
    
    private FxDetail(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
