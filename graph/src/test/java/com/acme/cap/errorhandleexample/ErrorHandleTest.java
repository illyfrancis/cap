package com.acme.cap.errorhandleexample;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;

public class ErrorHandleTest extends CamelTestSupport {

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {

            @Override
            public void configure() throws Exception {
                from("direct:input").bean(FirstBean.class).to("log:end");
            }
        };
    }

    public static class FirstBean {
        public String process(String body) {
            return String.format("processed by first [%s]", body);
        }
    }

}
