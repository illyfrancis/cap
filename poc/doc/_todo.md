## Notes

Should list only the todo items that can be checked off. The todo item should be derived from the issues list and must relate back to it.

Add a new item to ***Backlog*** then move the item to ***Todo*** section just before working on it. When it's done move the items to ***Done*** with date.

For issues, add to `_issues.md` and to record activities add it to `_logs.md`. And detailed design specific notes, include it to `design.md` etc.

## Todo

- Read up on Error handling (ch5)
- Need to continue to think about how to generate UTR message
- Similar to UC1 but even simpler
    - Config Camel context with Spring
    - Config dbconnection with Spring, connect to HSQLDB server mode (i.e. not embedded)
    - Persists a single message
    - Simulate exception (Duplicate etc) and force retry
- Read through how to configure and run as standalone app
    - http://camel.apache.org/running-camel-standalone.html
    - http://camel.apache.org/running-camel-standalone-and-have-it-keep-running.html
- Design how a cash transaction should be processed also with concurrency in mind
- Bean to persists input message
    - with graph - neo4j (done)
    - with jdbc
    - with camel-mongo [read this](http://camel.apache.org/mongodb.html)

##### UC1. Simple flow
1. Parse, transform message (to java object)
2. Lookup db with account # to determine for filtering
3. Filter
4. Store message (wireTap?)
5. Do dummy process
6. Store outbound message (wireTap?)
7. Send outbound queue (transform again? to what?)

## Backlog

#### Design doc

- Relabel **RTCR** to **Cash System** in design docs
- Include the new diagram to depict Infomediary as router to interface doc?

#### Use cases

#### IBM MQ specific

- Still need to configure connection pool for IBM MQ [how to do this?]
    - read up more on `org.springframework.jms.connection.CachingConnectionFactory`
- How to configure IBM WebSphere MQ connection factory & connection pool???
    - especially in comparison to ActiveMQ component
- JMS connection factory for WebSphere MQ (how?)
    - [more : New-JMS-connection-being-created-for-every-message](http://camel.465427.n5.nabble.com/New-JMS-connection-being-created-for-every-message-td5637735.html)
    - some [notes](http://camel.465427.n5.nabble.com/Camel-and-IBM-MQ-Series-td476223.html)
    - more [notes](http://camel.465427.n5.nabble.com/IBM-MQ-and-CAMEL-td5591661.html)
- MQ and transaction management

#### Messages

- Define format for Cash transaction
    - xml or protobuf or thrift or json ?

#### Persistence

- Read components (ch7)
    - working with databases
    - in memory messaging (Direct, SEDA, VM components)

#### Concurrency

- Read concurrency and scalability (ch10)
    - for concurrency, three different ways
        - using `.parallelProcessing` option on Splitter EIP
        - using a custom thread pool on Splitter EIP
        - using staged event-driven architecture (SEDA) with `?concurrentConsumers=20`
- Write a small test using Camel with worker threads
- Integrate Neo4j with Camel worker, so that worker can execute Cypher
- Refer to http://camel.apache.org/threading-model.html

#### Transactions

- Read transactions (ch9)
- MQ and transaction

#### Error handling

- Read error handling (ch5)
    - redelivery should be looked at again
- Failure and recovery (use TX?)
- http://camel.apache.org/exception-clause.html

#### Deployment & build

- Define how to run and deploy
    - define the run strategy (jar, java, maven etc) refer to ch13 & ch11
- Dev & test environment
    - what's the OS? where's the build system (i.e. Jenkins)

#### Testing support

- Read testing (ch6)
    - mock (a lot more in http://camel.apache.org/mock.html)
    - http://camel.apache.org/testing.html
- Look at guiceberry for integration test
    - http://code.google.com/p/guiceberry/
- Read up on ch4.3 Testing component in 'Dependency Injection: Design patterns using ...'
    - ch 4.3.4 for integration testing

#### Graph

- Thinking about the models in graph to best represent the UTR structures
- Capturing the use cases, and queries for GraphDB
- Need to document and capture the key info about
    - web console (localhost:7474/webconsole)
    - neo4j shell options (-readonly -path etc)
- Setup neo4j server
    - how to run server while Java embedded is running?
    - how to use neo4j shell while Java embedded is running?
- Compile a list of questions for Neo4j tutorial
    - running in VM, use of MMIO in VM

#### Audit & monitoring

- log strategy (refer to ch12)
    - log files
    - log component
    - tracer
        - configuration options
        - xml
        - code at context and at route level
        - with JMX (pretty cool!) & jconsole
    - notifications
- Organize issues list according to topics, e.g. transactions, persistence etc
- Log viewer to correlate per exchange ID in the logs

#### Akka

- Configure and create a small test using Akka to handle a simple message
- Think about how Akka can be used to represent the graph and how it can be recovered in a case of failure
- Integrate Akka example with Camel worker so that a message can be passed from Camel to Akka
- Ultimately I want this: `[ActiveMQ] --> [Camel] --> [Akka] --> [Camel] --> [ActiveMQ]`

## Done

### 8/25/2013 (Wed)

- Neo4j Workshop
    - can you run web console while using the db via Java app? (NO)
    - neo4j shell options (-readonly -path etc) while the db is in use by Java app? (NO)
- Alternative is to use REST api

#### 8/23/2013 & 8/24/2013 (Monday & Tuesday)

- Spring config
- RDB set up to use with Spring
    - HSQLDB
    - H2?

#### 8/20/2013 (Friday)

- Setup neo4j server
    - test with multiple threads each with the same transaction id and see how it copes
        - i.e. transaction(100, a) & transaction(100, b) & transaction(100, c) etc and see how many node gets created
            - didn't cope very well with non-test database
            - works well with TestGraphDatabaseFactory
- Think about using RDB in place of Graph?
- Model the core process with RDB instead of Graph
    - refer to `_process.md` file

#### 8/19/2013 (Thursday)

1. Look at how to use guice when unit testing, best strategy etc
    - Why use guice in unit testing?!? Just 'new' things for test!
- Setup neo4j server
    - try 'create or get' for creating a unique node
        - refer to `GraphRepository.java` but have some issues when testing with larger data (2000 or so), possibly due to it being run on Windows
    - test with multiple threads each with the same transaction id and see how it copes
        - i.e. transaction(100, a) & transaction(100, b) & transaction(100, c) etc and see how many node gets created

> inserted 1000 items (very slow) then inserted 200 (from index 2000 to 2200), whcih was a little faster. Then tried inserted further 200 then got exceptions from Neo. `org.neo4j.kernel.impl.nioneo.store.UnderlyingStorageException`. Found some references to it and appears to be related to mmio settings under Windows.


#### 8/18/2013 (Wednesday)

1. Setup neo4j server
    - downloaded both 2.0.0-M5 enterprise etc
    - tried console on http://localhost:7474/ and neo4j-shell
- neo4j index creation
    - explored Java legacy option
    - explored 2.0 Label index option
        - appears no Java api, only cypher or rest api
    - refer to `neo4j_index.md` file
- wrote a simple create etc
- wrote a simple case to use ExecutionEngine to use Cypher from Java
- set up simple graph test with guice
    - guice 3.0 is already in Maven central (so no need to install locally)

#### 8/17/2013 (Tuesday)

1. How to use Bean in the route
    - bean registry (read up on it)
    - just specify it? 
        - with Java you can specify ClassName.class without registering it first
    - or need to configure it with Spring?
        - ***haven't tried this yet***
    - does it create an instance every time?
        - doesn't appear so
- Reading up on transformation
    - can't get to use Jaxb via Java dsl
        - used it like `from("..").marshal(jaxb).to("..")`
    - try Spring config
        - ***haven't tried it***
- Feeder for Camel using ActiveMQ to feed some messages to Camel endpoint
    - there's activemq example producer
    - but need a nicer tool for producing chunkier message
    - refer to `Generator.java`

#### 8/16/2013 (Monday)

1. Locate the MQ jars and install in local maven repo
    - refer to `ibm_mq_maven.md` file
- Configure IBM MQ connection factory via Camel and create a simple consumer
    - refer to `IbmMqConsumer.java`
- Generate 1000 messages from ActiveMQ producer --> direct to IBM MQ --> direct to Foo bean --> log component
    - Start active mq `bin> activemq`
    - Start `{Classname}.java` from Eclipse or maven
    - Use `> ant producer -Dmax=1000`
    - MQ seems to struggle and very slow, possibly due to non-cached connection factory being created each time
    - Refer to `ActiveMqToIbmMq.java` and `MqRoundTrip.java`
- Try activeMq web console
    - http://localhost:8161/admin/queues.jsp (with admin/admin)

#### 8/13/2013 (Friday)

1. Create a first project structure using Maven without Spring
- Create a route that process a message from 'direct' endpoint and write to a log component
- Read about the log component and how to configure it
    - few options:
        - using the log: component
        - using DSL, e.g. `from("file://...").log("hello").bean(...)`
        - can even use `trace` for monitoring 
- Create a route that process a message from 'direct' endpoint and route it to log component with threads component
    - use `threads(p, m)` component, refer to `LogWithThreadsTest`
- set up and configure to use ActiveMQ locally
    - start up ActiveMQ
        - bin\activemq
    - first example (ch1 of ActiveMQ in Action) is to start consumer and producer
        - ant consumer & ant producer
- Configure ActiveMQ connection factory in Camel
    - use ActiveMQ component `ActiveMQComponent`
- Create a route that process a message from message queue and write to a log component
    - refer to `ActiveMQConsumer.java` and `JmsConsumer.java`
- Or come up with more specific example
    - [ActiveMQ] --> [JMS endpoint] --> [write to log]
    - [ActiveMQ] --> [JMS endpoint] --> [write to log in multiple threads]
- Intall IBM MQ locally
    - downloaded trial version from IBM site (7.5.0.2)
    - installed in C:\Program Files\IBM\WebSphere MQ
- Create JMS queue in IBM MQ
    - Created queue manager (QM_TEST)
    - Create JMS queue based on QM_TEST


#### 8/12/2013 and prior

1. Install & configure Camel
- Read CIA, chapter 1, 2 and some 3.
