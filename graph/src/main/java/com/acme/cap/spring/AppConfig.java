package com.acme.cap.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class AppConfig {

    @Bean
    MessagingService mockMessagingService() {
        return new MessagingService() {

            @Override
            public String getMessage() {
                return "Hello World";
            }
        };
    }

}
