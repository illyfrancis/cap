package com.xyz.cap;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

/**
 * Hello world!
 * 
 */
public class App {

	private static final String DB_PATH = "target/neo4j-hello-db";
	private GraphDatabaseService graphDb;

	public static void main(String[] args) {
		System.out.println("Hello World!");
		App app = new App();
		app.createDb();
		app.doSomething();
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

	private void doSomething() {
		System.out.println("Doing stuff");
	}
}
