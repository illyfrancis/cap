package com.acme.cap.zunk.visitorexample.generics;

public abstract class AbstractMessage<T> implements Message {

    @Override
    public Portfolio accept(Visitor visitor) {
        return null;    //visitor.visit((T)getMessage());
    }
    
    public abstract T getMessage();
}
