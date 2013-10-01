package com.acme.cap;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

import com.acme.cap.domain.Transaction;
import com.acme.cap.domain.Custody;
import com.acme.cap.message.CreateSnapshot;
import com.acme.cap.repository.DbRepository;

public class UtrRouteIntegrationTest extends CamelTestSupport {

    private static EmbeddedDatabase dataSource;

    @BeforeClass
    public static void prepareDataSource() {
        // EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        // EmbeddedDatabase db =
        // builder.setType(H2).addScript("my-schema.sql").addScript("my-test-data.sql").build();
        // do stuff against the db (EmbeddedDatabase extends
        // javax.sql.DataSource)
        // db.shutdown()

        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        dataSource = builder.addScript("db/schema.sql").addScript("db/data.sql").build();
    }

    @AfterClass
    public static void cleanupDataSource() {
        dataSource.shutdown();
    }

    @Override
    protected JndiRegistry createRegistry() throws Exception {
        DbRepository repository = new DbRepository();
        repository.setDataSource(dataSource);
        UtrMergeStrategy merger = new OverwriteNullStrategy();
        
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
    public void testSomething() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:output");
        Transaction cash = Custody.of(2, "REF_105", "A0001", 4009L);
        mock.expectedBodiesReceived(CreateSnapshot.newMessage("REF_105", cash, 102));

        String message = "2, REF_105, A0001, 4999";
        template.sendBody("direct:input", message);
        assertMockEndpointsSatisfied();
    }

    @Test
    public void testMockEndpoint() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:output");
        Transaction cash = Custody.of(1, "REF_101", "A0001", 4009L);
        mock.expectedBodiesReceived(CreateSnapshot.newMessage("REF_101", cash, 101));

        String message = "1, REF_101, A0001, 4999";
        template.sendBody("direct:input", message);

        assertMockEndpointsSatisfied();

        System.out.println(">>>>> " + mock.getReceivedCounter());
        List<Exchange> exchanges = mock.getReceivedExchanges();
        CreateSnapshot body = exchanges.get(0).getIn().getBody(CreateSnapshot.class);
        assertEquals("REF_101", body.getTransactionRef());
        // TODO - add other fields
    }
}
