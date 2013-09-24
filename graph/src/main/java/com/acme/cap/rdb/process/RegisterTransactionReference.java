package com.acme.cap.rdb.process;

import javax.inject.Inject;

import com.acme.cap.rdb.domain.Repository;
import com.acme.cap.rdb.message.CreateSnapshotMessage;
import com.acme.cap.rdb.message.RegisterTransactionReferenceMessage;

public class RegisterTransactionReference {

    private final Repository repository;

    @Inject
    public RegisterTransactionReference(Repository repository) {
        this.repository = repository;
    }

    public CreateSnapshotMessage process(RegisterTransactionReferenceMessage message) {
        long utrRegisterId = 0L;
        try {
            utrRegisterId = repository.createOrGetUtrRegister(message.getTransactionRef(),
                    message.getTransactionId());
        } catch (Exception e) {
            // TODO: handle exception
        }

        // update the source transaction data with the utrRegisterId
        repository.setUtrRegisterIdOnTransaction(message.getTransactionId(), utrRegisterId);

        // add 'utrRegisterId' to the next message
        // CoreMessage out = CoreMessage.Builder()
        return CreateSnapshotMessage.from(message.getTransactionRef(),
                message.getTransactionId(), utrRegisterId);
    }
}
