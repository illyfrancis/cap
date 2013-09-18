package com.acme.cap.graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.schema.IndexDefinition;
import org.neo4j.graphdb.schema.Schema;

public class WithCypher {
    private static final String DB_PATH = "target/neo4j-store-with-cypher";
    private GraphDatabaseService graphDb;

    public static void main(final String[] args) {

        WithCypher withCypher = new WithCypher();
        withCypher.startDb();
        withCypher.createIndex();
        withCypher.addUsers();
        withCypher.findUsers();
        withCypher.findUsersWithCypher();
        withCypher.resourceIterator();
        withCypher.updateUsers();
        withCypher.deleteUsers();
        withCypher.findUsers();
        withCypher.deleteUsersWithCypher();
        withCypher.findUsersWithCypher();
        withCypher.dropIndex();
        withCypher.shutdownDb();
    }

    private void startDb() {
        System.out.println("Starting database ...");
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);
    }

    private void shutdownDb() {
        System.out.println("Shutting down database ...");
        graphDb.shutdown();
    }

    private void dropIndex() {
        try (Transaction tx = graphDb.beginTx()) {
            Label label = DynamicLabel.label("User");
            for (IndexDefinition indexDefinition : graphDb.schema().getIndexes(label)) {
                // There is only one index
                indexDefinition.drop();
            }
            tx.success();
        }
    }

    private void deleteUsers() {
        System.out.println("Delete users");
        try (Transaction tx = graphDb.beginTx()) {
            Label label = DynamicLabel.label("User");
            int idToFind = 46;
            String nameToFind = "user" + idToFind + "@neo4j.org";

            for (Node node : graphDb.findNodesByLabelAndProperty(label, "username", nameToFind)) {
                System.out.println("within delete: " + node.getId());
                node.delete();
            }
            tx.success();
        }
    }

    private void updateUsers() {
        try (Transaction tx = graphDb.beginTx()) {
            Label label = DynamicLabel.label("User");
            int idToFind = 45;
            String nameToFind = "user" + idToFind + "@neo4j.org";

            for (Node node : graphDb.findNodesByLabelAndProperty(label, "username", nameToFind)) {
                node.setProperty("username", "user" + (idToFind + 1) + "@neo4j.org");
            }
            tx.success();
        }
    }

    private void resourceIterator() {
        Label label = DynamicLabel.label("User");
        int idToFind = 45;
        String nameToFind = "user" + idToFind + "@neo4j.org";

        try (Transaction transaction = graphDb.beginTx()) {
            ResourceIterator<Node> users = graphDb.findNodesByLabelAndProperty(label, "username", nameToFind)
                    .iterator();
            if (users.hasNext()) {
                Node firstUserNode = users.next();
                System.out.println("the first user is " + firstUserNode.getProperty("username"));
            }
            users.close();
            transaction.close();
        }
    }

    private void findUsers() {
        Label label = DynamicLabel.label("User");
        int idToFind = 45;
        String nameToFind = "user" + idToFind + "@neo4j.org";

        try (Transaction transaction = graphDb.beginTx()) {
            ResourceIterator<Node> users = graphDb.findNodesByLabelAndProperty(label, "username", nameToFind)
                    .iterator();
            ArrayList<Node> userNodes = new ArrayList<>();
            while (users.hasNext()) {
                userNodes.add(users.next());
            }

            for (Node node : userNodes) {
                System.out.println("The username of user " + idToFind + " is " + node.getProperty("username"));
            }
            transaction.close();
        }
    }

    private void deleteUsersWithCypher() {
        // execute a query
        ExecutionEngine engine = new ExecutionEngine(graphDb);
        try (Transaction tx = graphDb.beginTx()) {
            ExecutionResult result = engine.execute("match u:User delete u");


            String rows = "";
            for (Map<String, Object> row : result) {
                for (Entry<String, Object> column : row.entrySet()) {
                    rows += column.getKey() + " : " + column.getValue();
                }
                rows += "\n";
            }

            System.out.println("from deleteUsersWithCypher : " + rows);

            tx.success();
        }
    }

    private void addUsers() {
        try (Transaction tx = graphDb.beginTx()) {
            Label label = DynamicLabel.label("User");

            // Create some users and index their names with the new
            // IndexingService
            for (int id = 0; id < 100; id++) {
                Node userNode = graphDb.createNode(label);
                userNode.setProperty("username", "user" + id + "@neo4j.org");
            }
            System.out.println("Users created");
            tx.success();
        }
    }

    private void createIndex() {
        // createIndex
        IndexDefinition indexDefinition;
        try (Transaction tx = graphDb.beginTx()) {
            Schema schema = graphDb.schema();
            indexDefinition = schema.indexFor(DynamicLabel.label("User")).on("username").create();
            tx.success();
        }

        // wait for index
        try (Transaction tx = graphDb.beginTx()) {
            Schema schema = graphDb.schema();
            schema.awaitIndexOnline(indexDefinition, 10, TimeUnit.SECONDS);
            tx.close();
        }
    }

    private void findUsersWithCypher() {
            // execute a query
            ExecutionEngine engine = new ExecutionEngine(graphDb);
            try (Transaction tx = graphDb.beginTx()) {
                // ExecutionResult result =
                // engine.execute("start n=node(*) where n.name = 'my node' return n, n.name");
                ExecutionResult result = engine.execute("match u:User return count(u)");
    
    //            List<String> columns = result.columns();
    //            for (String n : columns) {
    //                System.out.println("columns : " + n);
    //            }
    //
    //            ResourceIterator<Node> n_column = result.columnAs("n");
    //            n_column.close();
    
                // Iterator<Node> n_column = result.columnAs("n");
                // for (Node node: IteratorUtil.asIterable(n_column)) {
                // String nodeResult = node + ": " + node.getProperty("name");
                // System.out.println(nodeResult);
                // }
    
                String rows = "";
                for (Map<String, Object> row : result) {
                    for (Entry<String, Object> column : row.entrySet()) {
                        rows += column.getKey() + " : " + column.getValue();
                    }
                    rows += "\n";
                }
    
                System.out.println("from findUsersWithCypher : " + rows);
                
            }
        }

}
