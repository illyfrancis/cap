package com.acme.cap.graph;

import java.util.Map;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;

import com.google.common.collect.ImmutableList;

public class GraphRepositoryTestHelper {

    // the query must return only one column and marked with "N".
    public static ImmutableList<Node> executeCypher(ExecutionEngine engine, String query,
            Map<String, Object> params) {
        ExecutionResult result = engine.execute(query, params);
        ResourceIterator<Node> iterator = result.columnAs("N");
        return ImmutableList.copyOf(iterator);
    }
}
