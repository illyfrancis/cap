package com.acme.cap.graph;

import java.util.Map;

import javax.inject.Inject;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

public class GraphRepository implements UtrRepository {

    private static final Logger log = LoggerFactory.getLogger(GraphRepository.class);

    private final GraphDatabaseService db;
    private final ExecutionEngine engine;
    final static String ACME = "ACME";

    @Inject
    public GraphRepository(GraphDatabaseService graphDatabaseService) {
        this.db = graphDatabaseService;
        this.engine = new ExecutionEngine(db);

        createClient(ACME);
    }

    // make sure the client node exist
    void createClient(String clientName) {
        try (Transaction tx = db.beginTx();
                ResourceIterator<Node> clients = db.findNodesByLabelAndProperty(
                        EntityLabel.CLIENT, "name", clientName).iterator()) {

            if (!clients.hasNext()) {
                Node client = db.createNode(EntityLabel.CLIENT);
                client.setProperty("name", clientName);
                tx.success();
                log.info("client node for {} created, with id [{}]", clientName, client.getId());
            } else {
                log.info("client node for {} exists", clientName);
            }
        }
    }

    @Override
    public long addIdentity(String ref) {
        log.info("adding identity for [{}]", ref);
        ImmutableList<Node> nodes;
        try (Transaction tx = db.beginTx()) {

            // create a unique Identity node for the client
            String query = "match c:CLIENT where c.name = {clientName} " +
                    "create unique (c)<-[:under]-(t:TXID {ref:{ref}}) " +
                    "return t as TXID";
            Map<String, Object> params = Maps.newHashMap();
            params.put("clientName", ACME); // for now
            params.put("ref", ref);

            ExecutionResult result = engine.execute(query, params);
            ResourceIterator<Node> iterator = result.columnAs("TXID");
            nodes = ImmutableList.copyOf(iterator);

            tx.success();
        }

        long nodeId = 0;
        if (nodes.size() > 0) {
            // TODO - should be throwing some sort of runtime exception if node
            // can't be retrieved.
            Node identity = nodes.get(0);
            nodeId = identity.getId();
        }

        return nodeId;
    }
}
