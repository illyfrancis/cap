package com.acme.cap.zunk.visitorexample.strategy;

import com.acme.cap.zunk.visitorexample.message.Message;
import com.acme.cap.zunk.visitorexample.message.Portfolio;

public interface Strategy {
    
    public Portfolio merge(Portfolio portfolio, Message message);
    
}
