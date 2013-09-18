package com.acme.cap.integration;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class TracerTest extends CamelTestSupport {

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {

            @Override
            public void configure() throws Exception {
                // enable trace
                context.setTracing(true);
                
                from("direct://wiretap").to("log://firstlog").wireTap("seda:audit").to("log://endlog");
                from("seda:audit").bean(AuditService.class, "auditFile");
            }
        };
    }

    @Test
    public void testLogWithWireTap() throws Exception {
        template.sendBody("direct://wiretap", "howdy");
        assertTrue(true);
    }
}
