package com.acme.cap.zunk.visitorexample.strategy;

import com.acme.cap.zunk.visitorexample.message.Cash;
import com.acme.cap.zunk.visitorexample.message.Portfolio;
import com.acme.cap.zunk.visitorexample.message.Share;

public interface PortfolioVisitor {
    public Portfolio visit(Portfolio portfolio, Cash cash);
    public Portfolio visit(Portfolio portfolio, Share share);
}
