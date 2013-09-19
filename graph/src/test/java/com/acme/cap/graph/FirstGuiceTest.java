package com.acme.cap.graph;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.test.TestGraphDatabaseFactory;

public class FirstGuiceTest {
    
    private GraphDatabaseService graphDb;

    @Before
    public void prepareTestDatabase() {
        graphDb = new TestGraphDatabaseFactory().newImpermanentDatabase();
    }

    @After
    public void destroyTestDatabase() {
        graphDb.shutdown();
    }

    @Test
    public void testFoo() {
        FirstGuice firstGuice = new FirstGuice(graphDb);
        firstGuice.checkIndexOnLabel(EntityLabel.ACCOUNT);
    }
}
