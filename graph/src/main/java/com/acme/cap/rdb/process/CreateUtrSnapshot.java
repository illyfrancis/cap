package com.acme.cap.rdb.process;

import javax.inject.Inject;

import com.acme.cap.rdb.domain.CashTransaction;
import com.acme.cap.rdb.domain.FxDetail;
import com.acme.cap.rdb.domain.MutualFundTransaction;
import com.acme.cap.rdb.domain.Repository;
import com.acme.cap.rdb.domain.UtrSnapshot;
import com.acme.cap.rdb.message.CreateSnapshotMessage;
import com.google.common.base.Optional;

public class CreateUtrSnapshot {
    private final Repository repository;

    @Inject
    public CreateUtrSnapshot(Repository repository) {
        this.repository = repository;
    }

    public void process(CreateSnapshotMessage message) {
        // TODO - wrap the whole thing in tx
        long utrRegisterId = message.getUtrRegisterId();

        // get all latest source data
        CashTransaction cashTransaction = repository.findLatestCashTransaction(utrRegisterId);
        MutualFundTransaction mutualFundTransaction = repository.findLatestMutualFundTransaction(utrRegisterId);
        FxDetail fxDetail = repository.findLatestFxDetail(utrRegisterId);
        
        // compare transactionId in the message with the latest transaction
        if (message.getTransactionId() != cashTransaction.getId()) {
            // TODO log this and stop further processing
            return;
        }
        
        // get the latest UtrSnapshot with utrRegisterId
        Optional<UtrSnapshot> latestUtrSnapshot = repository.findLatestUtrSnapShot(utrRegisterId);
        long nextVersion = 1L;
        if (latestUtrSnapshot.isPresent()) {
            nextVersion = latestUtrSnapshot.get().getVersion() + 1;
        }
        
        UtrSnapshot.Builder builder = new UtrSnapshot.Builder(utrRegisterId, nextVersion);
        Optional<CashTransaction> cash = Optional.fromNullable(cashTransaction);
        if (cash.isPresent()) {
            builder.cashTransactionId(cash.get().getId());
        }
        Optional<MutualFundTransaction> mf = Optional.fromNullable(mutualFundTransaction);
        if (mf.isPresent()) {
            builder.mutualFundTransactionId(mf.get().getId());
        }
        Optional<FxDetail> fx = Optional.fromNullable(fxDetail);
        if (fx.isPresent()) {
            builder.fxDetailId(fx.get().getId());
        }
        
        UtrSnapshot utrSnapshot = builder.build();
        long snapshotVersion = repository.createUtrSnapshot(utrSnapshot);
        
        // generate message for next process
    }
}
