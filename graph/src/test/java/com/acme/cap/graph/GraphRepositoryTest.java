package com.acme.cap.graph;

import static com.acme.cap.graph.GraphRepositoryTestHelper.*;
import static org.junit.Assert.*;

import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.test.TestGraphDatabaseFactory;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;

public class GraphRepositoryTest {

    private GraphDatabaseService db;
    private ExecutionEngine engine;

    @Before
    public void prepareTestDatabase() {
        db = new TestGraphDatabaseFactory().newImpermanentDatabase();
        engine = new ExecutionEngine(db);
    }

    @After
    public void destroyTestDatabase() {
        db.shutdown();
    }

    @Test
    public void testNodeExistAfterAddIdentity() {
        GraphRepository repository = new GraphRepository(db);
        String id = "123";
        long nodeId = repository.addIdentity(id);

        Node identity = null;
        Object ref = "";
        try (Transaction tx = db.beginTx()) {
            identity = db.getNodeById(nodeId);
            ref = identity.getProperty("ref");
        }

        assertNotNull(identity);
        assertEquals(id, ref);
    }

    @Test
    public void testSameNodeIdWhenAddingIdentityWithSameId() {
        GraphRepository repository = new GraphRepository(db);
        String id = "123";
        long firstId = repository.addIdentity(id);
        long secondId = repository.addIdentity(id);

        assertEquals(firstId, secondId);
    }

    @Test
    public void testAddIdentityByCount() {
        GraphRepository repository = new GraphRepository(db);
        String id = "1234", anotherId = "999";
        repository.addIdentity(id);
        repository.addIdentity(anotherId);
        repository.addIdentity(id);
        repository.addIdentity(id);

        ImmutableSet<Node> nodes;
        try (Transaction tx = db.beginTx()) {
            ResourceIterator<Node> identities = db
                    .findNodesByLabelAndProperty(EntityLabel.TXID, "ref", id).iterator();

            nodes = ImmutableSet.copyOf(identities);
            identities.close();
        }

        assertEquals(1, nodes.size());
    }

    @Test
    public void testOnlyOneIdentityIsCreatedWhenAddingWithMultipleRepository() {
        GraphRepository repoOne = new GraphRepository(db);
        GraphRepository repoTwo = new GraphRepository(db);

        // add
        String id = "234";
        repoOne.addIdentity(id);
        repoTwo.addIdentity(id);

        // need to verify that there are only one connected TXID node from CLIENT
        ImmutableList<Node> nodes;
        try (Transaction tx = db.beginTx()) {
        }
        String queryIdentities = "match c:CLIENT<-[:under]-(t:TXID) where c.name = {clientName} return t as N";
        Map<String, Object> params = Maps.newHashMap();
        params.put("clientName", GraphRepository.ACME);
        nodes = executeCypher(engine, queryIdentities, params);

        assertEquals(1, nodes.size());
    }
}
