package com.acme.cap;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.acme.cap.message.CreateSnapshot;

public class UtrRouteIntegrationTestWithSpring extends CamelSpringTestSupport {
    
    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/application-context.xml");
    }
    
    @Test
    public void testSomething() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:output");
        mock.expectedBodiesReceived(CreateSnapshot.newMessage("REF_101", 1, 101));
        
        String message = "1, REF_101, A0001, 4999";
        template.sendBody("direct:input", message);
        assertMockEndpointsSatisfied();
    }
    
    @Test
    public void testMockEndpoint() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:output");
        mock.expectedBodiesReceived(CreateSnapshot.newMessage("REF_101", 1, 101));
        
        String message = "1, REF_101, A0001, 4999";
        template.sendBody("direct:input", message);

        assertMockEndpointsSatisfied();

        System.out.println(">>>>> " + mock.getReceivedCounter());
        List<Exchange> exchanges = mock.getReceivedExchanges();
        CreateSnapshot body = exchanges.get(0).getIn().getBody(CreateSnapshot.class);
        assertEquals("REF_101", body.getTransactionRef());
        // TODO - add other fields
    }
}
