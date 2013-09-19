package com.cap.graph;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.test.TestGraphDatabaseFactory;

public class Neo4jBasicDocTest {

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
    public void simpleTest() {
        Transaction tx = graphDb.beginTx();
        Node n = null;
        try {
            n = graphDb.createNode();
            n.setProperty("name", "Nancy");
            tx.success();
        } catch (Exception e) {
            tx.failure();
        } finally {
            tx.finish();
        }

        assertThat(n.getId(), is(greaterThan(0L)));

        Transaction transaction = graphDb.beginTx();
        Node foundNode = graphDb.getNodeById(n.getId());

        assertThat(foundNode.getId(), is(n.getId()));
        assertThat((String) foundNode.getProperty("name"), is("Nancy"));
        transaction.finish();
    }

    @Test
    public void startWithConfiguration() {
        Map<String, String> config = new HashMap<String, String>();
        config.put("neostore.nodestore.db.mapped_memory", "10M");
        config.put("string_block_size", "60");
        config.put("array_block_size", "300");
        GraphDatabaseService db = new TestGraphDatabaseFactory().newImpermanentDatabaseBuilder()
                .setConfig(config).newGraphDatabase();
        db.shutdown();
    }
}
