package com.acme.cap.zunk.visitorexample.generics;

public class Portfolio {

    private final String cash;
    private final String share;
    
    public static Portfolio of(String cash, String share) {
        return new Portfolio(cash, share);
    }
    
    private Portfolio(String cash, String share) {
        this.cash = cash;
        this.share = share;
    }
    
    public String getCash() {
        return cash;
    }
    
    public String getShare() {
        return share;
    }
}
