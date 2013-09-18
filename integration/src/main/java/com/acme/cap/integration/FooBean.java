package com.acme.cap.integration;

public class FooBean {

    public String process(String body) {
        StringBuilder builder = new StringBuilder("processing-");
        builder.append(body);
        return builder.toString();
    }
}
