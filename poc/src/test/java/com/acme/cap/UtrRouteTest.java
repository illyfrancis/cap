package com.acme.cap;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;
import org.springframework.dao.DuplicateKeyException;

import com.acme.cap.repository.UtrRepository;

public class UtrRouteTest extends CamelTestSupport {

    @Override
    protected JndiRegistry createRegistry() throws Exception {
        UtrRepository repository = new UtrRepository() {
            
            @Override
            public void registerTransaction(long utrRegisterId, long transactionId) {
                // TODO - is there a better way to do this? possibly using Interceptor?
                throw new DuplicateKeyException("mock repository");
            }
            
            @Override
            public long getOrCreateRegister(String transactionRef) {
                return 0;
            }
        }; 
        
        UtrService service = new UtrService(repository);
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
        String message = "1, REF_101, A0001, 4999";
        template.sendBody("direct:input", message);
    }
}
