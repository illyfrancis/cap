package com.acme.cap.spring.hello;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                AppConfig.class);
        // ApplicationContext context = new
        // AnnotationConfigApplicationContext(AppConfig.class);
        CustomerDao customerDao = context.getBean(CustomerDao.class);
        customerDao.dodo();
        // context.registerShutdownHook();
        context.close();
    }
}