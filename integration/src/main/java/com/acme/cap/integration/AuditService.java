package com.acme.cap.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AuditService {
    
    private Logger logger = LoggerFactory.getLogger(AuditService.class);
    
    public void auditFile(String body) {
        System.out.println("> audit : " + body);
        logger.info("> audit : {}", body);
    }
}
