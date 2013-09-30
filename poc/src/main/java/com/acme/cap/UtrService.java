package com.acme.cap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acme.cap.domain.Transaction;
import com.acme.cap.domain.Custody;
import com.acme.cap.domain.UtrSnapshot;
import com.acme.cap.message.CreateSnapshot;
import com.acme.cap.message.RegisterReference;
import com.acme.cap.repository.UtrRepository;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;

public class UtrService {

    private static final Logger log = LoggerFactory.getLogger(UtrService.class);
    private UtrRepository repository;
    private UtrMergeStrategy mergeStrategy;

    public UtrService(UtrRepository repository) {
        this.repository = repository;
    }

    public RegisterReference filter(String message) {
        ImmutableList<String> col = ImmutableList.copyOf(Splitter.on(",").trimResults().split(message));
        Custody cash = Custody.of(
                Long.valueOf(col.get(0)),
                col.get(1),
                col.get(2),
                Long.valueOf(col.get(3)));

        log.info("Filtering [{}] - {}", message, cash);

        // save the transaction
        repository.save(cash);

        // create message and send out
        return RegisterReference.newMessage(cash.getReferenceId(), cash);
    }

    public CreateSnapshot registerReference(RegisterReference message) {
        log.info("Register [{}]", message);

        // 1. get or create UtrRegister from reference id
        String transactionRef = message.transactionRef();
        long utrRegisterId = repository.getOrCreateRegister(transactionRef);

        // 2. update transaction with the UtrRegisterId
        long transactionId = message.transaction().getId();
        repository.registerTransaction(utrRegisterId, transactionId);
        
        // 3. create next message
        return CreateSnapshot.newMessage(transactionRef, message.transaction(), utrRegisterId);
    }
    
    public void addSnapshot(CreateSnapshot message) {
        log.info("Add snapshot with [{}])", message);
        
        // within a transaction. tx.start();
        // 1. get the latest UtrSnapshot with utrRegisterId
        UtrSnapshot latest = repository.latestSnapshot(message.getUtrRegisterId());
        
        // 2. merge the transaction
        Transaction transaction = message.transaction();
        UtrSnapshot merged = mergeStrategy.merge(latest, transaction);
        
        // 3. save attempt, if duplicate retry via camel
        repository.addSnapshot(merged);
    }

}
