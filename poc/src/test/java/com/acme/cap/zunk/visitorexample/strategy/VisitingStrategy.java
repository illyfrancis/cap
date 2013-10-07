package com.acme.cap.zunk.visitorexample.strategy;

import com.acme.cap.zunk.visitorexample.message.Cash;
import com.acme.cap.zunk.visitorexample.message.Message;
import com.acme.cap.zunk.visitorexample.message.Portfolio;
import com.acme.cap.zunk.visitorexample.message.Share;

public class VisitingStrategy implements Strategy, PortfolioVisitor {

    @Override
    public Portfolio merge(Portfolio portfolio, Message message) {
        return message.accept(portfolio, this);
    }

    @Override
    public Portfolio visit(Portfolio portfolio, Cash cash) {
        return Portfolio.of(cash.getName(), portfolio.getShare());
    }

    @Override
    public Portfolio visit(Portfolio portfolio, Share share) {
        return Portfolio.of(portfolio.getCash(), share.getName());
    }

}
