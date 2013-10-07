package com.acme.cap.zunk.visitorexample.strategy;

import com.acme.cap.zunk.visitorexample.message.Cash;
import com.acme.cap.zunk.visitorexample.message.Portfolio;
import com.acme.cap.zunk.visitorexample.message.Share;

public class MergeVisitor implements Visitor {

    private final Portfolio portfolio;

    public MergeVisitor(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    @Override
    public Portfolio visit(Cash cash) {
        return Portfolio.of(cash.getName(), portfolio.getShare());
    }

    @Override
    public Portfolio visit(Share share) {
        return Portfolio.of(portfolio.getCash(), share.getName());
    }
    
}
