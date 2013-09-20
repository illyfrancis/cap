package com.acme.cap.generator;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acme.cap.graph.UtrRepository;

public class UtrCore {
    private static final Logger log = LoggerFactory.getLogger(UtrCore.class);
    private final UtrRepository repository;
    
    @Inject
    public UtrCore(UtrRepository repository) {
        this.repository = repository;
    }
    
//    public String process(String body) {
//        log.info("UtrCore:process with body [{}]", body);
//        String msg = repository.greetings();
//        return msg;
//    }
    
    public long identity(String ref) throws Exception {
        log.info("UtrCore:process with ref [{}]", ref);
        return repository.addIdentity(ref);
    }
}
