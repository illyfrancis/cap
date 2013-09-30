package com.acme.cap.repository;

import com.acme.cap.domain.Custody;
import com.acme.cap.domain.UtrSnapshot;

public interface UtrRepository {
    
    public void save(Custody transaction);
    
    public long getOrCreateRegister(String transactionRef);

    public void registerTransaction(long utrRegisterId, long transactionId);

    public UtrSnapshot latestSnapshot(long utrRegisterId);

    public void addSnapshot(UtrSnapshot merged);

}