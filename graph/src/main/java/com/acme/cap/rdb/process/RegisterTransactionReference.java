package com.acme.cap.rdb.process;

import javax.inject.Inject;

import com.acme.cap.rdb.domain.Repository;
import com.acme.cap.rdb.message.CoreMessage;

public class RegisterTransactionReference {

    private final Repository repository;

    @Inject
    public RegisterTransactionReference(Repository repository) {
        this.repository = repository;
    }

    public void process(CoreMessage message) {
        long utrRegisterId = 0L;
        try {
            utrRegisterId = repository.createOrGetUtrRegister(message.getTransactionRef(),
                    message.getTransactionId());
        } catch (Exception e) {
            // TODO: handle exception
        }
        
        // update the source transaction data with the utrRegisterId
        repository.setUtrRegisterIdOn(message.getTransactionId(), utrRegisterId);
    }
}
