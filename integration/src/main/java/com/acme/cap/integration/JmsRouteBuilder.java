package com.acme.cap.integration;

import org.apache.camel.builder.RouteBuilder;

public class JmsRouteBuilder extends RouteBuilder {

    public void configure() {
        from("jms:TEST.FOO").to("log:end");
    }

}
