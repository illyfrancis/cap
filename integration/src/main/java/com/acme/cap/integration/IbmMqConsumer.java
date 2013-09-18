package com.acme.cap.integration;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.component.jms.JmsConfiguration;
import org.apache.camel.impl.DefaultCamelContext;

import com.ibm.mq.jms.MQQueueConnectionFactory;

public class IbmMqConsumer {

    public static void main(String[] args) throws Exception {

        // from blog
        // http://camel.465427.n5.nabble.com/Camel-and-IBM-MQ-Series-td476223.html

        MQQueueConnectionFactory connectionFactory = new MQQueueConnectionFactory();
        connectionFactory.setHostName("localhost");
        connectionFactory.setPort(1414);
        connectionFactory.setQueueManager("QM_TEST");
        // connectionFactory.setChannel("channel");
        // connectionFactory.setTransportType(1);
        // connectionFactory.setUseConnectionPooling(true); // dep and no effect
        // on pooling
        JmsConfiguration configuration = new JmsConfiguration(connectionFactory);

        // JmsTransactionManager txm = new
        // JmsTransactionManager(connectionFactory);

        JmsComponent ibmmq = new JmsComponent(configuration);
        ibmmq.setAcknowledgementModeName("AUTO_ACKNOWLEDGE");

        // make it transacted
        // ibmmq.setTransacted(true);
        // ibmmq.setTransactionManager(txm);

        CamelContext context = new DefaultCamelContext();
        context.addComponent("ibmmq", ibmmq);

        context.addRoutes(new RouteBuilder() {

            @Override
            public void configure() throws Exception {
                from("ibmmq:ibm.foo").to("log:ibm mq");
                // from("file:").to("log:ibm mq");
                // from("file:data/inbox?noop=true").to("ibmmq:ibm.foo");
            }
        });

        context.start();

        Thread.sleep(30000);

        // stop the CamelContext
        context.stop();
    }
}
