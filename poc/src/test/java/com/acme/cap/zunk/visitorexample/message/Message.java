package com.acme.cap.zunk.visitorexample.message;

import com.acme.cap.zunk.visitorexample.strategy.PortfolioVisitor;
import com.acme.cap.zunk.visitorexample.strategy.Visitor;

public interface Message {
    public String getName();
    public Portfolio accept(Visitor visitor);
    public Portfolio accept(Portfolio portfolio, PortfolioVisitor visitor);
}
