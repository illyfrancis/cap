package com.acme.cap.graph;

import javax.inject.Singleton;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.test.TestGraphDatabaseFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class GraphModule extends AbstractModule {

    private static final String DB_PATH = "target/first-guice";
    
    @Override
    protected void configure() {

        // TODO - how do I use factory here?
//        bind(GraphDatabaseService.class).
    }
    
    @Provides @Singleton
    GraphDatabaseService provideGraphDatabaseService() {
        System.out.println("graph service...");
        GraphDatabaseService service = new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);
        return service;
    }

    // This is not needed in unit test. Just new the thing!
    @Provides @TestGraphDb @Singleton
    GraphDatabaseService provideTestGraphDatabaseService() {
        System.out.println("test graph service...");
        GraphDatabaseService service = new TestGraphDatabaseFactory().newImpermanentDatabase();
        return service;
    }

}
