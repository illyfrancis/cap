package com.acme.cap;

import static org.mockito.Mockito.*;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import com.acme.cap.repository.UtrRepository;

public class UtrRouteTest extends CamelTestSupport {

    @Override
    protected JndiRegistry createRegistry() throws Exception {
        UtrRepository repository = mock(UtrRepository.class);
        UtrMergeStrategy merger = mock(UtrMergeStrategy.class);
        UtrService service = new UtrService(repository, merger);
        JndiRegistry jndi = super.createRegistry();
        jndi.bind("utrService", service);
        return jndi;
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new UtrRoute();
    }

    @Test
    public void testRetry() {
        context.setTracing(true);
        String message = "1, REF_101, A0001, 4999";
        template.sendBody("direct:input", message);
    }
}
