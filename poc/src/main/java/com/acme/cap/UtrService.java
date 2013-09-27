package com.acme.cap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acme.cap.domain.Transaction;
import com.acme.cap.message.CreateSnapshot;
import com.acme.cap.message.RegisterReference;
import com.acme.cap.repository.UtrRepository;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;

public class UtrService {

    private static final Logger log = LoggerFactory.getLogger(UtrService.class);
    private UtrRepository repository;

    public UtrService(UtrRepository repository) {
        this.repository = repository;
    }

    public RegisterReference filter(String message) {
        ImmutableList<String> col = ImmutableList.copyOf(Splitter.on(",").trimResults().split(message));
        Transaction cash = Transaction.of(
                Long.valueOf(col.get(0)),
                col.get(1),
                col.get(2),
                Long.valueOf(col.get(3)));

        log.info("Filtering [{}] - {}", message, cash);

        // TODO - save Transaction in db
        // repository.save(cash);

        // create message and send out
        return RegisterReference.newMessage(cash.getReferenceId(), cash.getId());
    }

    public CreateSnapshot registerReference(RegisterReference message) {
        log.info("Register [{}]", message);

        // 1. get or create UtrRegister from reference id
        String transactionRef = message.transactionRef();
        long utrRegisterId = repository.getOrCreateRegister(transactionRef);

        // 2. update transaction with the UtrRegisterId
        long transactionId = message.transactionId();
        repository.registerTransaction(utrRegisterId, transactionId);
        
        return CreateSnapshot.newMessage(transactionRef, transactionId, utrRegisterId);
    }

}
