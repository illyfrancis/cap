# Index in Neo4j

There are different 'schemes' to use indexes in Neo4j. The first is the 'legacy' index on *Node* and *Relationship*. And the other approach is the new `2.0` way of indexing on the *label*.

## Questions!!!!

How to create unique node using each scheme?

## Legacy Index

There is UniqueFactory but I think it's discouraged now.

## Label Index

#### Index creation

It appears that since `2.0` you can add index to *label*s. And this is described in the example `EmbeddedNeo4jWithNewIndexing.java` from [first link](http://docs.neo4j.org/chunked/preview/tutorials-java-embedded-new-index.html) or [second link](http://docs.neo4j.org/chunked/milestone/tutorials-java-embedded-new-index.html). (***they are pointing to slightly different information !!!***)

So basically, we need to create an index on the label. According to the [second link](http://docs.neo4j.org/chunked/milestone/tutorials-java-embedded-new-index.html) do this:

    GraphDatabaseSerivce graphDb = new GraphDatabaseFactory.newEmbeddedDatabase( DB_PATH);

    // configure the database to index users by name. Only needs to be done once.
    IndexDefinition indexDefinition;
    try (Transaction tx = graph.beginTx()) {
    	Schema schema = graphDb.schema();
    	indexDefinition = schema.indexFor( DynamicLabel.label("User"))
    		.on("username")
    		.create();
    }

Cypher equivalent would be:

	CREATE INDEX ON :User(username)

> Note, to drop the index `DROP INDEX ON :User(username)`, according to [link](http://docs.neo4j.org/chunked/milestone/query-schema-index.html).

#### Wait for index population

Indexes are populated asynchronously when they are first created. It is possible to use the core API to wait for index population to complete.

    try (Transaction tx = graphDb.beginTx()) {
    	Schema schema = graphDb.schema();
    	schema.awaitIndexOnline( indexDefinition, 10, TimeUnit.SECONDS );
    }

#### Adding entity/node

	try (Transaction tx = graphDb.beginTx()) {
		Label label = DynamicLabel.label("User");

		// Create some users
		for (int id = 0; id < 100; id++) {
			Node userNode = graphDb.createNode(label);
			userNode.setProperty("username", "user" + id + "@neo4j.org");
		}
		System.out.println("Users created");
		tx.success();
	}

#### Query using Label & Property

	Label label = DynamicLabel.label("User");
	int idToFind = 45;
	String nameToFind = "user" + idToFind + "@neo4j.org";
	try (Transaction tx = graphDb.beginTx()) {
		ResourceIterator<Node> users = graphDb.findNodesByLabelAndProperty(label, "username", nameToFind).iterator();

		ArrayList<Node> userNodes = new ArrayList<>();
		while (users.hasNext()) {
			userNodes.add(users.next());
		}

		for (Node node : userNodes) {
			System.out.println("The username of user " + idToFind + "is " + node.getProperty("username"));
		}
	}

#### Updating Node

When updating the name of a user, the index is updated as well:

	try (Transaction tx = graphDb.beginTx()) {
		Label label = DynamicLabel.label("User");
		int idToFind = 45;
		String nameToFind =  "user" + idToFind + "@neo4j.org";

		for (Node node: graphDb.findNodesByLabelAndProperty(label, "username", nameToFind)) {
			node.setProperty("username", "user" + (idToFind + 1) + "@node4j.org");
		}
		tx.success();
	}

#### Deleting

When deleting a user, it is automatically removed from the index.

	try (Transaction tx = graphDb.beginTx()) {
		Label label = DynamicLabel.label("User");
		int idToFind = 46;
		String nameToFind = "user" + idToFind + "@neo4j.org";

		for (Node node: graphDb.findNodesByLabelAndProperty(label, "username", nameToFind)) {
			node.delete();
		}
		tx.success();

	}

#### Droping index

In case we change our data model, drop the index.

	try (Transasction tx = graphDb.beginTx()) {
		Label label = DynamicLabel.label("User");
		for (IndexDefinition indexDefinition : graphDb.schema().getIndexes(label)) {
			// There is only one index
			indexDefinition.drop();
		}
		tx.success();
	}

Cypher equivalent would be:

	DROP INDEX ON :User(username)


#### Label can be Enums

Instead of using string e.g. "User", the labels can be typed by implementing `Label`.

## How to create unique node?

#### Using label index

Only reference I have found is by using Cypher and no Java core api. Refering to [this](http://stackoverflow.com/questions/16253386/how-to-use-auto-indexes-with-neo4j-2-0-labels):

There is no such things as:

	create unique (n:User {username:"bob"})

Intead uniquness is enforced via

	create unique (b)-[:ISUSER]->(m:User {username:"bob"})

or use `merge` like

	merge (n:User {username:"bob"})
	return n

> My concern/question is are the uniqueness truly based on the index? i.e. are the following two considered a same node? ***NO considered as different, see example below***

	create unique (b)-[:ISUSER]->(m:User {username:"bob"})

and

	create unique (b)-[:ISUSER]->(m:User {username:"bob", age:10})

> Need to verify and test it to confirm. Also the second question is how to do this in Java?

#### Example for using label index in Cypher

	// create index
	create index on :User(username);

	// create client node
	create (c:Client {name:"ACME"}) return c;

	// create a unique User with username "bill"
	match c:Client 
	where c.name = "ACME"
	create unique (c)<-[:under]-(u:User {username:"bill"})
	return u;

Which should create a User node.

	// executing it again
	match c:Client 
	where c.name = "ACME"
	create unique (c)<-[:under]-(u:User {username:"bill"})
	return u;

Should just return the same node and behaves as expected.

How about a node with additional property "age:10" but with the same "username" on which the index is created with.

	match c:Client 
	where c.name = "ACME"
	create unique (c)<-[:under]-(u:User {username:"bill", age:10})
	return u;

Strangely it treats it as different node and creates a new User **:-(** Not good.