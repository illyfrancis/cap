package com.acme.cap.integration;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.camel.spi.DataFormat;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class TransformXmlWithJaxbTest extends CamelTestSupport {

//    @Before
//    public void before() {
//        DataFormat jaxb = new JaxbDataFormat("com.acme.cap.integration");
//        DataFormatDefinition jaxbDefinition = new DataFormatDefinition(jaxb);
//        Map<String, DataFormatDefinition> dataFormats = new HashMap<>();
//        dataFormats.put("jaxb", jaxbDefinition);
//        context.setDataFormats(dataFormats);
//    }

//    @Before
//    public void didntWork() {
//        org.apache.camel.model.dataformat.JaxbDataFormat jaxbDefinition = new org.apache.camel.model.dataformat.JaxbDataFormat();
//        jaxbDefinition.setContextPath("com.acme.cap.integration");
//        Map<String, DataFormatDefinition> dataFormats = new HashMap<>();
//        dataFormats.put("jaxb", jaxbDefinition);
//        context.setDataFormats(dataFormats);
//    }
    
    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {

            @Override
            public void configure() throws Exception {
                
                // it needs jaxb.index file
                DataFormat jaxb = new JaxbDataFormat("com.acme.cap.integration");
                from("direct:input").to("log:start").marshal(jaxb).to("log:middle").unmarshal(jaxb)
                        .to("log:end");

                // the route config doesn't work due to Jaxb error. refer to
                // Ch3 for more information on configuration.
                // from("direct:input").to("log:start").marshal().jaxb().to("log:middle").unmarshal().jaxb()
                // .to("log:end");
            }
        };
    }

    @Test
    public void testTransform() {
        PurchaseOrder order = PurchaseOrder.of("bar", 10.49, 100.00);
        template.sendBody("direct:input", order);
    }
}
