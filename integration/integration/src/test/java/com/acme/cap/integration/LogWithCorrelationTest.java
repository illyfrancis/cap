package com.acme.cap.integration;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class LogWithCorrelationTest extends CamelTestSupport {

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {

            @Override
            public void configure() throws Exception {
                from("direct://inbox")
                    .to("log://output?showExchangeId=true")
                    .bean(FooBean.class)
                    .to("log://end?showExchangeId=true");
                from("direct://dsllog").log("${id} - hello ${body}");
            }
        };
    }

    @Test
    public void testLogs() throws Exception {
        template.sendBody("direct://inbox", "hello");
        template.sendBody("direct://dsllog", "ET");
    }
}
