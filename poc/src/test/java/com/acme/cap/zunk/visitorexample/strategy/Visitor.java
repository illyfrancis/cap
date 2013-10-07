package com.acme.cap.zunk.visitorexample.strategy;

import com.acme.cap.zunk.visitorexample.message.Cash;
import com.acme.cap.zunk.visitorexample.message.Portfolio;
import com.acme.cap.zunk.visitorexample.message.Share;

public interface Visitor {
    public Portfolio visit(Cash cash);
    public Portfolio visit(Share share);
}
