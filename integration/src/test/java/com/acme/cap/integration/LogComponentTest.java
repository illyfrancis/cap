package com.acme.cap.integration;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Ignore;
import org.junit.Test;

public class LogComponentTest extends CamelTestSupport {

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {

            @Override
            public void configure() throws Exception {
                from("direct://inbox").to("log://output");
                
                from("direct://wiretap").to("log://firstlog").wireTap("seda:audit").to("log://end");
                from("seda:audit").bean(AuditService.class, "auditFile");
                
                from("direct://dsllog").log("hello ${body}");
            }
        };
    }

    @Test
    public void testLogDirect() throws Exception {
        template.sendBody("direct://inbox", "hello");
        assertTrue(true);
    }

    @Ignore
    @Test
    public void testLogDirectMultiple() throws Exception {
        for (int i = 0; i < 10; i++) {
            template.sendBody("direct://inbox", "hello-"+i);
        }
    }

    @Test
    public void testLogWithWireTap() throws Exception {
        template.sendBody("direct://wiretap", "howdy");
        assertTrue(true);
    }
    
    @Test
    public void testLogWithDSL() {
        template.sendBody("direct://dsllog", "wendy");
    }
}
