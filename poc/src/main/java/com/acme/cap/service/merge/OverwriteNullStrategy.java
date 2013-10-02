package com.acme.cap.service.merge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acme.cap.domain.CorporateAction;
import com.acme.cap.domain.Custody;
import com.acme.cap.domain.FXDetail;
import com.acme.cap.domain.MutualFund;
import com.acme.cap.domain.Transaction;
import com.acme.cap.domain.UtrSnapshot;

public class OverwriteNullStrategy implements UtrMergeStrategy {

    private static Logger logger = LoggerFactory.getLogger(OverwriteNullStrategy.class);

    @Override
    public UtrSnapshot merge(UtrSnapshot latest, Transaction transaction) {
        UtrSnapshot snapshot = null;

        // FIXME - replace instanceof
        if (transaction instanceof Custody) {
            snapshot = mergeCustody(latest, (Custody) transaction);
        } else if (transaction instanceof CorporateAction) {
            snapshot = mergeCorporateAction(latest, (Custody) transaction);
        } else if (transaction instanceof MutualFund) {
            snapshot = mergeMutualFund(latest, (MutualFund) transaction);
        } else if (transaction instanceof FXDetail) {
            snapshot = mergeFxDetail(latest, (FXDetail) transaction);
        }

        return snapshot;
    }

    private UtrSnapshot mergeFxDetail(UtrSnapshot latest, FXDetail transaction) {
        logger.info("mergeFxDetail");
        return null;
    }

    private UtrSnapshot mergeMutualFund(UtrSnapshot latest, MutualFund transaction) {
        logger.info("mergeMutualFund");
        return null;
    }

    private UtrSnapshot mergeCorporateAction(UtrSnapshot latest, Custody transaction) {
        logger.info("mergeCorporateAction");
        return null;
    }

    private UtrSnapshot mergeCustody(UtrSnapshot latest, Custody custody) {
        logger.info("mergeCustody");

        UtrSnapshot.Builder builder = new UtrSnapshot.Builder(latest.getUtrRegisterId(),
                latest.getVersion() + 1)
                .amount(latest.getAmount())
                .currency(latest.getCurrency())
                .accountNumber(latest.getAccountNumber());

        if (latest.getAccountNumber() == null) {
            builder.accountNumber(custody.getAccountNumber());
        }

        if (latest.getAmount() == null) {
            builder.amount(custody.getAmount());
        }

        if (latest.getCurrency() == null) {
            builder.currency(custody.getCurrency());
        }

        return builder.build();
    }
}
