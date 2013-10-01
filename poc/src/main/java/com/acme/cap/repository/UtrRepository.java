package com.acme.cap.repository;

import com.acme.cap.domain.Custody;
import com.acme.cap.domain.UtrMessage;
import com.acme.cap.domain.UtrSnapshot;

public interface UtrRepository {
    
    public void saveTransaction(Custody transaction);
    
    public long getOrCreateRegister(String transactionRef);

    public void registerTransaction(long utrRegisterId, long transactionId);

    public UtrSnapshot getLatestSnapshot(long utrRegisterId);

    public void saveSnapshot(UtrSnapshot merged);

    public UtrMessage getLastestUtrMessage(long utrRegisterId);

}