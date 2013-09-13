package com.acme.cap.integration;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class LogWithThreadsTest extends CamelTestSupport {

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {

            @Override
            public void configure() throws Exception {
                from("seda:start").to("log:A?showExchangeId=true").threads(5, 10).to("log:B?showExchangeId=true");
            }
        };
    }
    
    @Test
    public void testLoggingWithThreads() {
        for (int i = 0; i < 10; i++) {
            template.sendBody("seda:start", "hello-" + i);
        }
    }
}
