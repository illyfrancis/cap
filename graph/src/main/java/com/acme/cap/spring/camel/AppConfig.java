package com.acme.cap.spring.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class AppConfig {

    @Bean
    public CamelContext getCamelContext() throws Exception {
        DefaultCamelContext context = new DefaultCamelContext();
        context.addRoutes(new DefaultRoute());
        return context;
    }
}
