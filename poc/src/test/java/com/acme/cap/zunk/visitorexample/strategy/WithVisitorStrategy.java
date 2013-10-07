package com.acme.cap.zunk.visitorexample.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acme.cap.zunk.visitorexample.message.Message;
import com.acme.cap.zunk.visitorexample.message.Portfolio;

public class WithVisitorStrategy implements Strategy {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Portfolio merge(Portfolio portfolio, Message message) {
        Visitor visitor = new MergeVisitor(portfolio);
        return message.accept(visitor);
    }
}
