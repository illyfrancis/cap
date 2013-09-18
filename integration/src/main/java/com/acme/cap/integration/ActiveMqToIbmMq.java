package com.acme.cap.integration;

import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.component.jms.JmsConfiguration;
import org.apache.camel.impl.DefaultCamelContext;

import com.ibm.mq.jms.MQQueueConnectionFactory;

public class ActiveMqToIbmMq {

    public static void main(String[] args) throws Exception {

        // IBM MQ connection factory
        MQQueueConnectionFactory mqConnectionFactory = new MQQueueConnectionFactory();
        mqConnectionFactory.setHostName("localhost");
        mqConnectionFactory.setPort(1414);
        mqConnectionFactory.setQueueManager("QM_TEST");

        JmsConfiguration configuration = new JmsConfiguration(mqConnectionFactory);
        JmsComponent ibmmq = new JmsComponent(configuration);
        ibmmq.setAcknowledgementModeName("AUTO_ACKNOWLEDGE");

        CamelContext context = new DefaultCamelContext();
        context.addComponent("ibmmq", ibmmq);

        // add ActiveMQ component
        context.addComponent("activemq", ActiveMQComponent.activeMQComponent("tcp://localhost:61616"));

        context.addRoutes(new RouteBuilder() {

            @Override
            public void configure() throws Exception {
                // from("activemq:TEST.FOO").threads(5, 5).to("ibmmq:ibm.foo");
                from("activemq:TEST.FOO").to("ibmmq:ibm.foo").to("activemq:TEST.BAR");
            }
        });

        context.start();

        Thread.sleep(60000);

        // stop the CamelContext
        context.stop();
    }
}
