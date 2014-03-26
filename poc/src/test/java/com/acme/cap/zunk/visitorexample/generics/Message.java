package com.acme.cap.zunk.visitorexample.generics;

public interface Message {
    public String getName();

    public Portfolio accept(Visitor visitor);

    public Portfolio accept(Portfolio portfolio, PortfolioVisitor visitor);
}
