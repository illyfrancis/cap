package com.acme.cap.rdb.domain;


public class UtrRegister {
    private final long id;
    private final String transactionRef;
    
    public static UtrRegister build() {
        return new UtrRegister(1, "12345");
    }

    // Ideally UtrRegister should have ref to all transactions like this
    // <code>private List<Transaction> transactions;</code> but  

    private UtrRegister(long id, String transactionRef) {
        this.id = id;
        this.transactionRef = transactionRef;
    }

    public long getId() {
        return id;
    }

    public String getTransactionRef() {
        return transactionRef;
    }
}
