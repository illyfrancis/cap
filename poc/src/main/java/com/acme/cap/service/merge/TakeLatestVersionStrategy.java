package com.acme.cap.service.merge;

import com.acme.cap.domain.Transaction;
import com.acme.cap.domain.UtrSnapshot;

public class TakeLatestVersionStrategy implements UtrMergeStrategy {

    @Override
    public UtrSnapshot merge(UtrSnapshot latest, Transaction transaction) {
        // TODO Auto-generated method stub
        return null;
    }

}
