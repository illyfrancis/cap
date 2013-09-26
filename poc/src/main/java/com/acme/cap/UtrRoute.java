package com.acme.cap;

import org.apache.camel.builder.RouteBuilder;

public class UtrRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        // TODO - define route
        from("direct:input").to("mock:output");
    }
}
