package com.acme.cap;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

// TODO - the route test should be implemented and integration test
// or use NotifyBuilder (http://camel.apache.org/notifybuilder.html) and ch6
public class UtrRouteTest extends CamelTestSupport {
    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new UtrRoute();
    }

    @Test
    public void testSomething() throws Exception {
        // MockEndpoint mock = getMockEndpoint("mock:queue.order");
        MockEndpoint mock = getMockEndpoint("mock:output");
        mock.expectedBodiesReceived("hello");
        template.sendBody("direct:input", "hello");
        assertMockEndpointsSatisfied();
    }
    
    @Test
    public void testMockEndpoint() {
        MockEndpoint mock = getMockEndpoint("mock:output");
        template.sendBody("direct:input", "hello");

        System.out.println(">>>>> " + mock.getReceivedCounter());
        List<Exchange> exchanges = mock.getReceivedExchanges();
        String body = exchanges.get(0).getIn().getBody(String.class);
        assertTrue(body.contains("hello"));
    }
}
