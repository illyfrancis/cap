package com.acme.cap.spring.camel;

import org.apache.camel.builder.RouteBuilder;

public class DefaultRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("file:data/inbox?noop=true").to("log:output");
    }
}
