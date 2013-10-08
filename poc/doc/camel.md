## Error handling

#### Types

##### 1. Recoverable

Use `Exchange` with `setException(Throwable cause)` and `Exception getException()`.

##### 2. Irrecoverable

Represent an irrecoverable error as a message with a fault flag set.

    Message msg = Exchange.getOut();
    msg.setFault(true);
    msg.setBody("Unknown customer");

#### Internal to Camel

    try {
      processor.process(exchange);
    } catch (Throwable e) {
        exchange.setException(e);
    }

#### Error handlers in Camel

Error handlers in Camel only trigger when `exchange.getException() != null`.

#### Types of error handlers in Camel

- DefaultErrorHandler - auto enabled, in case no other has been configured
- DeadLetterChannel
- TransactionErrorHandler - transaction-aware error handler
- NoErrorHandler - used to disable error handling
- LogginErrorHandler - just logs the exception

The first three extend the `redeliveryErrorHandler`.

#### Remember there are 'channel's within a route

#### Default error handler

Configured with settings:
- No redelivery
- Exceptions are propagated back to the caller

## JMS

### Connection Factory Configuration

Both **Spring** and **Java DSL** are equivalent below.

> By default, JMS `ConnectionFactory` doesn't pool connections to the broker, so it will spin up new connections for every message. See next section that uses connection factories that use connection pooling.

#### Spring

Define JMS component with `ActiveMQConnectionFactory`.

    <bean id="jms" class="org.apache.camel.component.jms.JmsComponent">
      <property name="connectionFactory">
        <bean class="org.apache.activemq.ActiveMQConnectionFactory">
          <property name="brokerURL" value="vm://localhost" />
        </bean>
      </property>
    </bean>

Then use it in route as

    <to uri="jms:incomingOrders"/>

#### Java DSL

    public static void main(String args[]) throws Exception {
        // create CamelContext
        CamelContext context = new DefaultCamelContext();
        
        // connect to embedded ActiveMQ JMS broker
        ConnectionFactory connectionFactory = 
            new ActiveMQConnectionFactory("vm://localhost");

        context.addComponent("jms",
            JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));

Then use it in routes as

        // add our route to the CamelContext
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() {
                from("ftp://rider.com/orders?username=rider&password=secret").to("jms:incomingOrders");
            }
        });

### Use ActiveMQ specific component instead

ActiveMQ component configures connection pooling automatically.

    <bean id="activemq" class="org.apache.activemq.camel.component.ActiveMQComponent">
      <property name="brokerURL" value="tcp://localhost:61616" />
    </bean>

