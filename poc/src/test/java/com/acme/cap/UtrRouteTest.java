package com.acme.cap;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;
import org.springframework.dao.DuplicateKeyException;

import com.acme.cap.domain.Custody;
import com.acme.cap.domain.Transaction;
import com.acme.cap.message.CreateSnapshot;
import com.acme.cap.message.GenerateUtr;
import com.acme.cap.message.RegisterReference;
import com.acme.cap.service.UtrService;
import com.google.common.base.Joiner;

public class UtrRouteTest extends CamelTestSupport {

    UtrService service = mock(UtrService.class);

    @Override
    protected JndiRegistry createRegistry() throws Exception {
        JndiRegistry jndi = super.createRegistry();
        jndi.bind("utrService", service);
        return jndi;
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new UtrRoute();
    }
    
//    @Override
//    public String isMockEndpoints() {
//    }
    
//    @Override
//    public String isMockEndpointsAndSkip() {
//        return "log:*";
//    }

    @Test
    public void testThatRouteRetriesWhenRegisterReferenceThrowsDuplicateKeyException() throws Exception {

        RegisterReference registerReference = mock(RegisterReference.class);
        when(service.filter(anyString())).thenReturn(registerReference);
        when(service.registerReference(registerReference))
                .thenThrow(new DuplicateKeyException("rego failed"));

        // expectation on the mock end point
        MockEndpoint mock = getMockEndpoint("mock:output");
        mock.expectedMessageCount(0);

        template.sendBody("direct:input", "some message");

        // make sure to give enough time for route to complete
        Thread.sleep(2000);

        assertMockEndpointsSatisfied();

        verify(service).filter(anyString());
        verify(service, times(3)).registerReference(registerReference);
        verify(service, never()).addSnapshot(any(CreateSnapshot.class));
        verify(service, never()).generateUtrMessage(any(GenerateUtr.class));
    }

    @Test
    public void testThatRouteRetriesOnlyOnceWhenRegisterReferenceThrowsExceptionThenComesGood()
            throws Exception {

        RegisterReference registerReference = mock(RegisterReference.class);
        CreateSnapshot createSnapshot = mock(CreateSnapshot.class);
        when(service.filter(anyString())).thenReturn(registerReference);
        when(service.registerReference(registerReference))
                .thenThrow(new DuplicateKeyException("rego failed"))
                .thenReturn(createSnapshot);

        // expectation on the mock end point
        MockEndpoint mock = getMockEndpoint("mock:output");
        mock.expectedMessageCount(1);

        template.sendBody("direct:input", "some message");

        // make sure to give enough time for route to complete
        Thread.sleep(2000);

        assertMockEndpointsSatisfied();

        verify(service).filter(anyString());
        verify(service, times(2)).registerReference(registerReference);
        verify(service).addSnapshot(any(CreateSnapshot.class));
        verify(service, never()).generateUtrMessage(any(GenerateUtr.class));
    }

    @Test
    public void testThatRouteRetriesWhenAddSnapshotThrowsDuplicateKeyException() throws Exception {

        RegisterReference registerReference = mock(RegisterReference.class);
        when(service.filter(anyString())).thenReturn(registerReference);
        CreateSnapshot createSnapshot = mock(CreateSnapshot.class);
        when(service.registerReference(registerReference)).thenReturn(createSnapshot);
        when(service.addSnapshot(createSnapshot))
                .thenThrow(new DuplicateKeyException("ex from add snapshot"));

        // expectation on the mock end point
        MockEndpoint mock = getMockEndpoint("mock:output");
        mock.expectedMessageCount(0);

        template.sendBody("direct:input", "some message");

        // make sure to give enough time for route to complete
        Thread.sleep(2000);

        assertMockEndpointsSatisfied();

        verify(service).filter(anyString());
        verify(service).registerReference(registerReference);
        verify(service, times(3)).addSnapshot(any(CreateSnapshot.class));
        verify(service, never()).generateUtrMessage(any(GenerateUtr.class));
    }

    @Test
    public void testThatRouteRetriesOnlyOnceWhenAddSnapshotThrowsExceptionThenComesGood()
            throws Exception {

        RegisterReference registerReference = mock(RegisterReference.class);
        CreateSnapshot createSnapshot = mock(CreateSnapshot.class);
        GenerateUtr generateUtr = mock(GenerateUtr.class);
        when(service.filter(anyString())).thenReturn(registerReference);
        when(service.registerReference(registerReference)).thenReturn(createSnapshot);
        when(service.addSnapshot(createSnapshot))
            .thenThrow(new DuplicateKeyException("ex from add snapshot"))
            .thenReturn(generateUtr);

        // expectation on the mock end point
        MockEndpoint mock = getMockEndpoint("mock:output");
        mock.expectedMessageCount(1);

        template.sendBody("direct:input", "some message");

        // make sure to give enough time for route to complete
        Thread.sleep(2000);

        assertMockEndpointsSatisfied();

        verify(service).filter(anyString());
        verify(service).registerReference(registerReference);
        verify(service, times(2)).addSnapshot(any(CreateSnapshot.class));
        verify(service, never()).generateUtrMessage(any(GenerateUtr.class));
    }

    @Test
    public void testNormalRoute() throws Exception {
        context.setTracing(true);

        long transactinoId = 102L;
        String transactionRef = "REF_101";
        String accountNumber = "A-001";
        long amount = 299L;
        long utrRegisterId = 456L;
        String message = Joiner.on(",").join(transactinoId, transactionRef, accountNumber, amount);

        // stub filter
        Transaction cash = Custody.of(transactinoId, transactionRef, accountNumber, amount);
        RegisterReference registerReference = RegisterReference.newMessage(transactionRef, cash);
        when(service.filter(message)).thenReturn(registerReference);

        // stub register reference
        CreateSnapshot createSnapshot = CreateSnapshot.newMessage(transactionRef, cash, utrRegisterId);
        when(service.registerReference(registerReference)).thenReturn(createSnapshot);

        // stub add snapshot
        GenerateUtr generateUtr = mock(GenerateUtr.class);
        when(service.addSnapshot(createSnapshot)).thenReturn(generateUtr);

        // expectation on the mock end point
        MockEndpoint mock = getMockEndpoint("mock:output");
        mock.expectedBodiesReceived(generateUtr);

        template.sendBody("direct:input", message);

        // just to give some time for route to complete processing
        // not needed when asserting mock end point
        // Thread.sleep(10);

        assertMockEndpointsSatisfied();

        verify(service).filter(message);
        verify(service).registerReference(registerReference);
        verify(service).addSnapshot(createSnapshot);
        verify(service, never()).generateUtrMessage(any(GenerateUtr.class));
    }

}
