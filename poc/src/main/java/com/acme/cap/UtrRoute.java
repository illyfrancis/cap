package com.acme.cap;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.dao.DuplicateKeyException;

public class UtrRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        // TODO - define route
        from("direct:input").routeId("[utr-r1]").tracing()
                .beanRef("utrService", "filter")
                .to("seda:utr-r2");
        from("seda:utr-r2").routeId("[utr-r2]").tracing()
                .onException(DuplicateKeyException.class)
                    .maximumRedeliveries(2)
                    .backOffMultiplier(2)
                    .redeliveryDelay(1000)
                    .retryAttemptedLogLevel(LoggingLevel.WARN)
                    .handled(true)
                    .to("log:exception")
                    .end()
//                .errorHandler(defaultErrorHandler()
//                        .maximumRedeliveries(2)
//                        .backOffMultiplier(2)
//                        .redeliveryDelay(1000)
//                        .retryAttemptedLogLevel(LoggingLevel.WARN))
                .beanRef("utrService", "registerReference")
                .to("mock:output");
    }
}
