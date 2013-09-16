package com.acme.cap.integration;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class TransformXmlWithXstreamTest extends CamelTestSupport {

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {

            @Override
            public void configure() throws Exception {
                 from("direct:input").to("log:start").marshal().xstream().to("log:middle").unmarshal().xstream().to("log:end");
            }
        };
    }

    @Test
    public void testTransform() {
        PurchaseOrder order = new PurchaseOrder();
        order.setAmount(100.00);
        order.setName("bar");
        order.setPrice(10.49);
        
        template.sendBody("direct:input", order);
    }

    public static class PurchaseOrder {
        private String name;
        private double price;
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
