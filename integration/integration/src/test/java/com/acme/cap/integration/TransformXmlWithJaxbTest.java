package com.acme.cap.integration;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class TransformXmlWithJaxbTest extends CamelTestSupport {
    
    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        
        return new RouteBuilder() {

            @Override
            public void configure() throws Exception {
                 from("direct:input").to("log:start").marshal().jaxb().to("log:middle").unmarshal().jaxb().to("log:end");
            }
        };
    }

    @Test
    public void testTransform() {
//        template.sendBody("direct:input", "<TxId>12345<TxId>");
        PurchaseOrder order = new PurchaseOrder();
        order.setAmount(100.00);
        order.setName("bar");
        order.setPrice(10.49);
        
        template.sendBody("direct:input", order);
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class PurchaseOrder {
        @XmlAttribute
        private String name;
        @XmlAttribute
        private double price;
        @XmlAttribute
        private double amount;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

    }
}
