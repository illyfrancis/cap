package com.acme.cap.zunk.visitorexample.generics;

public interface Visitor {
    public Portfolio visit(Cash cash);

    public Portfolio visit(Share share);
}
