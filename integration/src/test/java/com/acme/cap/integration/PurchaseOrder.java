package com.acme.cap.integration;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PurchaseOrder {
    @XmlAttribute
    private String name;
    @XmlAttribute
    private double price;
    @XmlAttribute
    private double amount;
    
    public PurchaseOrder() {
        // must have no arg default constructor for JAXB to work
    }
    
    private PurchaseOrder(String name, double price, double amount) {
        this.name = name;
        this.price = price;
        this.amount = amount;
    }

    public static PurchaseOrder of(String name, double price, double amount) {
        return new PurchaseOrder(name, price, amount);
    }
}