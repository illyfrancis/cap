package com.acme.cap.zunk.visitorexample.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acme.cap.zunk.visitorexample.message.Cash;
import com.acme.cap.zunk.visitorexample.message.Message;
import com.acme.cap.zunk.visitorexample.message.Portfolio;
import com.acme.cap.zunk.visitorexample.message.Share;

public class InstanceOfStrategy implements Strategy {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Portfolio merge(Portfolio portfolio, Message message) {
        if (message instanceof Cash) {
            return merge(portfolio, (Cash) message);
        } else if (message instanceof Share) {
            return merge(portfolio, (Share) message);
        }
        return null;
    }

    Portfolio merge(Portfolio portfolio, Cash message) {
        return Portfolio.of(message.getName(), portfolio.getShare());
    }

    Portfolio merge(Portfolio portfolio, Share message) {
        return Portfolio.of(portfolio.getCash(), message.getName());
    }
}
