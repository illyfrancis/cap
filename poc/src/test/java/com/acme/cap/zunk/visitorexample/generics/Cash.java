package com.acme.cap.zunk.visitorexample.generics;

public class Cash implements Message {

    private final String name;

    public static Cash of(String name) {
        return new Cash(name);
    }

    private Cash(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Portfolio accept(Visitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public Portfolio accept(Portfolio portfolio, PortfolioVisitor visitor) {
        return visitor.visit(portfolio, this);
    }
}