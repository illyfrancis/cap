package com.acme.cap.spring.camel;

import org.apache.camel.CamelContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

public class MainApp {

    public static void main(String[] args) throws Exception {
        try (GenericApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);) {
            context.registerShutdownHook();
            CamelContext camel = context.getBean(CamelContext.class);
            System.out.println("Auto : " + camel.isAutoStartup());
            camel.start();

//            ProducerTemplate template = new DefaultProducerTemplate(camel);
//            template.start();
//            int i = 0;
//            do {
//                template.sendBody("direct:input", String.format("Hello [%d]", ++i));
//            } while (true);
////            template.stop();
            
//            do {
//            } while (true);
            Thread.sleep(1000);
        }
    }
}
