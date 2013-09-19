package com.acme.cap.graph;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.schema.IndexDefinition;
import org.neo4j.graphdb.schema.Schema;
import org.neo4j.graphdb.schema.Schema.IndexState;

import com.google.common.base.Optional;

public class FirstGuice {

    private final GraphDatabaseService db;
    private final ExecutionEngine engine;

    @Inject
    public FirstGuice(GraphDatabaseService graphDatabaseService) {
        this.db = graphDatabaseService;
        this.engine = new ExecutionEngine(db);
    }

    public void shutdownDb() {
        System.out.println("shutting down...");
        db.shutdown();
    }

    public void checkIndexOnLabel(Label label) {
        try (Transaction tx = db.beginTx()) {
            Schema schema = db.schema();

            boolean hasIt = isIndexOnLabelCreated(schema, label);
            System.out.format("has it [%s]\n", hasIt);

            Iterable<IndexDefinition> indexes = schema.getIndexes(label);

            for (IndexDefinition indexDefinition : indexes) {
                IndexState state = schema.getIndexState(indexDefinition);
                System.out.format("Index [%s] with state [%s]\n", indexDefinition.getLabel(), state);
            }
        }
    }

    private boolean isIndexOnLabelCreated(Schema schema, Label label) {
        Iterable<IndexDefinition> indexes = schema.getIndexes(label);
        return indexes.iterator().hasNext();
    }

    public void createIndexOnLabelWithProperty(Label label, String property) {
        Optional<IndexDefinition> index = Optional.absent();
        try (Transaction tx = db.beginTx()) {
            Schema schema = db.schema();
            if (!isIndexOnLabelCreated(schema, label)) {
                System.out.println("creating index");
                index = Optional.of(schema.indexFor(label).on(property).create());
                tx.success();
            } else {
                System.out.println("index already there, so not creating...");
            }
        }

        try (Transaction tx = db.beginTx()) {
            Schema schema = db.schema();
            if (index.isPresent()) {
                schema.awaitIndexOnline(index.get(), 10, TimeUnit.SECONDS);
            }
        }
    }

    void dropIndexOnLabel(Label label) {
        try (Transaction tx = db.beginTx()) {
            Schema schema = db.schema();
            for (IndexDefinition indexDefinition : schema.getIndexes(label)) {
                System.out.println("droping index ..");
                // There is only one index
                indexDefinition.drop();
            }
            tx.success();
        }
    }
   
}
