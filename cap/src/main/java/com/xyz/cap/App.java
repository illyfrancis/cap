package com.xyz.cap;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

/**
 * Hello world!
 * 
 */
public class App {

    private static final String DB_PATH = "target/neo4j-hello-db";
    private GraphDatabaseService graphDb;
    Node firstNode;
    Node secondNode;
    Relationship relationship;

    private static enum RelTypes implements RelationshipType {
        KNOWS
    }

    public static void main(String[] args) {
        System.out.println("Hello World!");
        App app = new App();
        app.createDb();
        app.createData();
        app.removeData();
        app.shutDown();
    }

    void createDb() {
        System.out.println("Create db");
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);

        System.out.println("Register shutdown");
        registerShutdownHook(graphDb);
    }

    private static void registerShutdownHook(final GraphDatabaseService graphDb) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.out.println("GraphDb shutdown by hook");
                graphDb.shutdown();
            }
        });
    }

    private void shutDown() {
        System.out.println("Manual shutdown");
        graphDb.shutdown();
    }

    private void createData() {
        System.out.println("Doing stuff");
        Transaction tx = graphDb.beginTx();
        try {
            // updating operations go here
            firstNode = graphDb.createNode();
            firstNode.setProperty("message", "Hello, ");
            secondNode = graphDb.createNode();
            secondNode.setProperty("message", "World!");

            relationship = firstNode.createRelationshipTo(secondNode, RelTypes.KNOWS);
            relationship.setProperty("message", "brave Neo4j");

            System.out.println(firstNode.getProperty("message"));
            System.out.println(relationship.getProperty("message"));
            System.out.println(secondNode.getProperty("message"));

            tx.success();
        } finally {
            tx.finish();
        }
    }

    private void removeData() {
        System.out.println("Removing data");
        Transaction tx = graphDb.beginTx();
        try {
            firstNode.getSingleRelationship(RelTypes.KNOWS, Direction.OUTGOING).delete();
            firstNode.delete();
            secondNode.delete();
            tx.success();
        } finally {
            tx.finish();
        }
    }
}
