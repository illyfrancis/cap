package com.acme.cap.generator;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.test.TestGraphDatabaseFactory;

import com.acme.cap.graph.GraphRepository;

public class IntegrationTest extends CamelTestSupport {

    private static GraphDatabaseService db;
    private static UtrCore utrCore;

    @BeforeClass
    public static void prepareTestDatabase() {
        db = new TestGraphDatabaseFactory().newImpermanentDatabase();
        // db = new
        // GraphDatabaseFactory().newEmbeddedDatabase("target/integration-db");
        GraphRepository repository = new GraphRepository(db);
        utrCore = new UtrCore(repository);
    }

    @AfterClass
    public static void destroyTestDatabase() {
        db.shutdown();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {

            @Override
            public void configure() throws Exception {
                from("direct:input")
                        .threads(5, 5)
                        .bean(utrCore)
                        .to("log:output");
                // from("direct:input").to("seda:process").end().to("log:end");
                // from("seda:process?concurrentConsumers=20")
                // .bean(utrCore);
            }
        };
    }

    @Ignore
    @Test
    public void testFeedSingle() {}

    // @Ignore
    @Test
    public void testFeedMultiple() {
        for (int i = 2200; i < 2400; i++) {
            // int ref = (i % 7) + 1;
            int ref = i + 1;
            template.sendBody("direct:input", String.valueOf(ref));
        }
    }
}
