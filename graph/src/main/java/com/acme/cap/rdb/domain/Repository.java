package com.acme.cap.rdb.domain;

import com.google.common.base.Optional;


public interface Repository {

    // putIfAbsent semantic, must be within db transaction
    public abstract long createOrGetUtrRegister(String transactionRef, long transactionId) throws Exception;

    public abstract void setUtrRegisterIdOnTransaction(long transactionId, long utrRegisterId);

    public abstract CashTransaction findLatestCashTransaction(long utrRegisterId);

    public abstract MutualFundTransaction findLatestMutualFundTransaction(long utrRegisterId);

    public abstract FxDetail findLatestFxDetail(long utrRegisterId);

    public abstract Optional<UtrSnapshot> findLatestUtrSnapShot(long utrRegisterId);

    public abstract long createUtrSnapshot(UtrSnapshot utrSnapshot);

}