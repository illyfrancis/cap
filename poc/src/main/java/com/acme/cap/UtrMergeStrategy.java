package com.acme.cap;

import com.acme.cap.domain.Transaction;
import com.acme.cap.domain.UtrSnapshot;

public interface UtrMergeStrategy {
    public UtrSnapshot merge(UtrSnapshot latest, Transaction trasaction);
}
