package com.acme.cap.generator;

import java.util.Random;

public class MessageGenerator {

    private static Random randomGenerator = new Random();

    public static Transaction generate() {
        int id = randomGenerator.nextInt(10) + 1;
        return Transaction.of(Integer.toString(id), "123", "pending");
    }
}
