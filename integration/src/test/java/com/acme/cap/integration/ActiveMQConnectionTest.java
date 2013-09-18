package com.acme.cap.integration;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class ActiveMQConnectionTest extends CamelTestSupport {

    @Override
    protected CamelContext createCamelContext() throws Exception {

//        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost");

        CamelContext camelContext = super.createCamelContext();
        camelContext.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));
        return camelContext;
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {

            @Override
            public void configure() throws Exception {
                from("direct:input").to("jms:TEST.FOO").to("log:end");
            }
        };
    }

    @Test
    public void testSend() {
        template.sendBody("direct:input", "hello");
    }
}
