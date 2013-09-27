package com.acme.cap.repository;

public interface UtrRepository {

    public long getOrCreateRegister(String transactionRef);

    public void registerTransaction(long utrRegisterId, long transactionId);

}