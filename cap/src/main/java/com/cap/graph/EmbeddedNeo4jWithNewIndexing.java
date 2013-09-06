package com.cap.graph;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.schema.IndexDefinition;
import org.neo4j.graphdb.schema.Schema;

public class EmbeddedNeo4jWithNewIndexing {
    private static final String DB_PATH = "target/neo4j-store-with-new-indexing";

    public static void main(final String[] args) {
        System.out.println("Starting database ...");

        // START SNIPPET: startDb
        GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);
        // END SNIPPET: startDb

        {
            // START SNIPPET: createIndex
            IndexDefinition indexDefinition;
            Transaction tx = graphDb.beginTx();
            try {
                Schema schema = graphDb.schema();
                indexDefinition = schema.indexFor(DynamicLabel.label("User")).on("username").create();
                tx.success();
            } finally {
                tx.finish();
            }
            // END SNIPPET: createIndex
            // START SNIPPET: wait
            Transaction transaction = graphDb.beginTx();
            try {
                Schema schema = graphDb.schema();
                schema.awaitIndexOnline(indexDefinition, 10, TimeUnit.SECONDS);
            } finally {
                transaction.finish();
            }
            // END SNIPPET: wait
        }

        {
            // START SNIPPET: addUsers
            Transaction tx = graphDb.beginTx();
            try {
                Label label = DynamicLabel.label("User");

                // Create some users and index their names with the new
                // IndexingService
                for (int id = 0; id < 100; id++) {
                    Node userNode = graphDb.createNode(label);
                    userNode.setProperty("username", "user" + id + "@neo4j.org");
                }
                System.out.println("Users created");
                tx.success();
            } finally {
                tx.finish();
            }
            // END SNIPPET: addUsers
        }

        {
            // START SNIPPET: findUsers
            Label label = DynamicLabel.label("User");
            int idToFind = 45;
            String nameToFind = "user" + idToFind + "@neo4j.org";
            Transaction transaction = graphDb.beginTx();
            try {
                ResourceIterator<Node> users = graphDb.findNodesByLabelAndProperty(label, "username",
                        nameToFind).iterator();
                ArrayList<Node> userNodes = new ArrayList<>();
                while (users.hasNext()) {
                    userNodes.add(users.next());
                }

                for (Node node : userNodes) {
                    System.out.println("The username of user " + idToFind + " is "
                            + node.getProperty("username"));
                }
            } finally {
                transaction.finish();
            }
            // END SNIPPET: findUsers
        }

        {
            // START SNIPPET: resourceIterator
            Label label = DynamicLabel.label("User");
            int idToFind = 45;
            String nameToFind = "user" + idToFind + "@neo4j.org";
            Transaction transaction = graphDb.beginTx();
            try {
                ResourceIterator<Node> users = graphDb.findNodesByLabelAndProperty(label, "username",
                        nameToFind).iterator();
                if (users.hasNext()) {
                    Node firstUserNode = users.next();
                    System.out.println("the first use is " + firstUserNode.getProperty("username"));
                }
                users.close();
            } finally {
                transaction.finish();
            }
            // END SNIPPET: resourceIterator
        }

        {
            // START SNIPPET: updateUsers
            Transaction tx = graphDb.beginTx();
            try {
                Label label = DynamicLabel.label("User");
                int idToFind = 45;
                String nameToFind = "user" + idToFind + "@neo4j.org";

                for (Node node : graphDb.findNodesByLabelAndProperty(label, "username", nameToFind)) {
                    node.setProperty("username", "user" + (idToFind + 1) + "@neo4j.org");
                }
                tx.success();
            } finally {
                tx.finish();
            }
            // END SNIPPET: updateUsers
        }

        {
            // START SNIPPET: deleteUsers
            Transaction tx = graphDb.beginTx();
            try {
                Label label = DynamicLabel.label("User");
                int idToFind = 46;
                String nameToFind = "user" + idToFind + "@neo4j.org";

                for (Node node : graphDb.findNodesByLabelAndProperty(label, "username", nameToFind)) {
                    node.delete();
                }
                tx.success();
            } finally {
                tx.finish();
            }
            // END SNIPPET: deleteUsers
        }

        {
            // START SNIPPET: dropIndex
            Transaction tx = graphDb.beginTx();
            try {
                Label label = DynamicLabel.label("User");
                for (IndexDefinition indexDefinition : graphDb.schema().getIndexes(label)) {
                    // There is only one index
                    indexDefinition.drop();
                }

                tx.success();
            } finally {
                tx.finish();
            }
            // END SNIPPET: dropIndex
        }

        System.out.println("Shutting down database ...");
        // START SNIPPET: shutdownDb
        graphDb.shutdown();
        // END SNIPPET: shutdownDb
    }

}