package com.acme.cap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acme.cap.domain.Custody;
import com.acme.cap.domain.Transaction;
import com.acme.cap.domain.UtrMessage;
import com.acme.cap.domain.UtrSnapshot;
import com.acme.cap.message.CreateSnapshot;
import com.acme.cap.message.GenerateUtr;
import com.acme.cap.message.RegisterReference;
import com.acme.cap.repository.UtrRepository;
import com.google.common.base.Optional;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

public class UtrService {

    private static final Logger log = LoggerFactory.getLogger(UtrService.class);
    private UtrRepository repository;
    private UtrMergeStrategy mergeStrategy;

    public UtrService(UtrRepository repository, UtrMergeStrategy mergeStrategy) {
        this.repository = repository;
        this.mergeStrategy = mergeStrategy;
    }

    public RegisterReference filter(String message) {
        ImmutableList<String> col = ImmutableList.copyOf(Splitter.on(",").trimResults().split(message));
        Custody cash = Custody.of(
                Long.valueOf(col.get(0)),
                col.get(1),
                col.get(2),
                Long.valueOf(col.get(3)));

        log.info("filtering : transformed [{}] to {}", message, cash);

        // save the transaction
        repository.saveTransaction(cash);

        // create message and send out
        return RegisterReference.newMessage(cash.getReferenceId(), cash);
    }

    public CreateSnapshot registerReference(RegisterReference message) {
        log.info("registering [{}]", message);

        // 1. get or create UtrRegister from reference id
        String transactionRef = message.transactionRef();
        long utrRegisterId = repository.getOrCreateRegister(transactionRef);

        // 2. update transaction with the UtrRegisterId
        long transactionId = message.transaction().getId();
        repository.registerTransaction(utrRegisterId, transactionId);

        // 3. create next message
        return CreateSnapshot.newMessage(transactionRef, message.transaction(), utrRegisterId);
    }

    public GenerateUtr addSnapshot(CreateSnapshot message) {
        log.info("adding snapshot with [{}])", message);

        // within a transaction. tx.start();
        // 1. get the latest UtrSnapshot with utrRegisterId
        UtrSnapshot latest = repository.getLatestSnapshot(message.getUtrRegisterId());

        // 2. merge the transaction
        Transaction transaction = message.transaction();
        UtrSnapshot merged = mergeStrategy.merge(latest, transaction);

        log.info("merged : [{}], latest:[{}] with transaction:[{}]", merged, latest, transaction);

        // 3. save attempt, if duplicate retry via camel
        repository.saveSnapshot(merged);

        return GenerateUtr.newMessage(message.getTransactionRef(), merged);
    }

    public void generateUtrMessage(GenerateUtr message) {
        log.info("generating UTR with [{}]", message);

        // validate if needed then stop if invalid
        // could implement as content based router,
        // from(...).choice().when(predicate).to(...).otherwise().to(...) [refer
        // to ch2]

        // retrieve latest UtrMessage
        Optional<UtrMessage> latest = Optional.of(repository.getLastestUtrMessage(message.getUtrSnapshot()
                .getUtrRegisterId()));
        
        // hash the current
        HashFunction hf = Hashing.murmur3_128();
        HashCode hc = hf.newHasher()
                .putObject(message.getUtrSnapshot(), new UtrSnapshot.UtrSnapshotFunnel())
                .hash();
        
        long signature = hc.asLong();
        
        if (latest.isPresent()) {
            if (signature != latest.get().getSignature()) {
                // store it and send
            }
        } else {
            // create new and send
        }
    }

}
