package com.acme.cap.rdb.process;

import com.acme.cap.rdb.domain.CashTransaction;
import com.acme.cap.rdb.domain.MutualFundTransaction;
import com.acme.cap.rdb.message.RegisterTransactionReferenceMessage;

public class CoreMessageBuilder {

    public RegisterTransactionReferenceMessage produce(CashTransaction transaction) {
        return RegisterTransactionReferenceMessage.of(transaction);
    }
    
    public RegisterTransactionReferenceMessage produce(MutualFundTransaction transaction) {
        return RegisterTransactionReferenceMessage.of(transaction);
    }
}
