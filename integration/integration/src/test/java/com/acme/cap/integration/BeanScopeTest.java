package com.acme.cap.integration;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class BeanScopeTest extends CamelTestSupport {

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            
            @Override
            public void configure() throws Exception {
                from("direct:input").bean(SomeBean.class).to("log:end");
            }
        };
    }
    
    public static class SomeBean {
        public String doSomething(String body) {
            return this.toString();
        }
    }
    
    @Test
    public void testIsBeanSingleton() {
        
        SomeBean fooBean = new SomeBean();
        log.info("The foo {}", fooBean.toString());
        
        template.sendBody("direct:input", "hello");
        template.sendBody("direct:input", "howdy");
        template.sendBody("direct:input", "hola");
        
        // how to verify that it uses the same instance of SomeBean?
        // I can see in the log but how to assert that?
    }
}
