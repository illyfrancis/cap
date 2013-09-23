package com.acme.cap.rdb.domain;

public interface Repository {

    // putIfAbsent semantic, must be within db transaction
    public abstract long createOrGetUtrRegister(TransactionRef transactionRef, long transactionId) throws Exception;

    public abstract void setUtrRegisterIdOn(long transactionId, long utrRegisterId);

}