package com.acme.cap.zunk.visitorexample.generics;

public interface PortfolioVisitor {
    public Portfolio visit(Portfolio portfolio, Cash cash);

    public Portfolio visit(Portfolio portfolio, Share share);
}
