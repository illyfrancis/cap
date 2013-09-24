package com.acme.cap.rdb.domain;

public interface Repository {

    // putIfAbsent semantic, must be within db transaction
    public abstract long createOrGetUtrRegister(String transactionRef, long transactionId) throws Exception;

    public abstract void setUtrRegisterIdOnTransaction(long transactionId, long utrRegisterId);

}