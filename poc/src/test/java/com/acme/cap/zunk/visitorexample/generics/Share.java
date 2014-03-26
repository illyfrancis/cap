package com.acme.cap.zunk.visitorexample.generics;

public class Share implements Message {

    private final String name;

    public static Share of(String name) {
        return new Share(name);
    }

    private Share(String name) {
        super();
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
