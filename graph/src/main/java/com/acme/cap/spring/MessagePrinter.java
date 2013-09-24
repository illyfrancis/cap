package com.acme.cap.spring;

import javax.inject.Inject;
import javax.inject.Named;

//@Component
@Named
public class MessagePrinter {

    // @Autowired
    @Inject
    private MessagingService service;

    public void printMessage() {
        System.out.println(this.service.getMessage());
    }
}
