package space.gavinklfong.insurance.quotation.mockserver;

import org.mockserver.client.MockServerClient;
import org.mockserver.client.initialize.PluginExpectationInitializer;
import org.mockserver.mock.Expectation;
import org.mockserver.model.MediaType;
import org.mockserver.server.initialize.ExpectationInitializer;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class QuotationEngineInitialization implements PluginExpectationInitializer {

    @Override
    public void initializeExpectations(MockServerClient mockServerClient) {
        MockServerExpectationInitializer initializer = new MockServerExpectationInitializer(mockServerClient);
        initializer.initializeForIntegrationTest();
    }
}
