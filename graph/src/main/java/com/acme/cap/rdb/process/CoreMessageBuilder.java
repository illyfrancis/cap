package com.acme.cap.rdb.process;

import com.acme.cap.rdb.domain.CashTransaction;
import com.acme.cap.rdb.message.CoreMessage;

public class CoreMessageBuilder {

    public CoreMessage produce(CashTransaction transaction) {
        return CoreMessage.of(transaction);
    }
}
