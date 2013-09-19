package com.acme.cap.graph;

import org.neo4j.graphdb.Label;

public enum EntityLabel implements Label {
    CLIENT,
    UTR,
    TXID,
    TRANSACTION,
    ACCOUNT
}
