package space.gavinklfong.insurance.quotation.apiclients;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.MediaType;
import org.mockserver.springtest.MockServerTest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import space.gavinklfong.insurance.quotation.dtos.QuotationEngineReq;
import space.gavinklfong.insurance.quotation.models.Customer;
import space.gavinklfong.insurance.quotation.models.Product;
import space.gavinklfong.insurance.quotation.models.Quotation;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@Slf4j
@MockServerTest("server.url=http://localhost:${mockServerPort}")
@ExtendWith(SpringExtension.class)
public class QuotationEngineClientTests {

    @Value("${server.url}")
    private String serverUrl;

    private MockServerClient mockServerClient;

    @Test
    void givenQuotationReq_generateQuotation() {

        // Setup request matcher and response using OpenAPI definition
        mockServerClient
                .when(
                        request()
                                .withMethod("POST")
                                .withPath("/quotation/generate")
                )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withContentType(MediaType.APPLICATION_JSON)
                                .withBody(
                                        "{" +
                                                " \"quotationCode\": \"b2430bfb-0de4-405f-91f8-4719b117dc5f\"," +
                                                " \"amount\": 12500," +
                                                " \"productCode\": \"CAR001-003\"," +
                                                " \"customerId\": 1," +
                                                " \"expiryTime\": \"2020-01-23T14:50:20\" " +
                                                "}"
                                )
                );

        // Initialize API client and trigger request
        QuotationEngineClient quotationEngineClient = new QuotationEngineClient(serverUrl);
        QuotationEngineReq req = QuotationEngineReq.builder()
                .customer(Customer.builder().id(1l).dob(LocalDate.of(1990, 01, 01)).name("Customer Name").build())
                .product(Product.builder().productCode("CAR001-003").postCodeInService(new String[]{"SW20"}).productPlan("Home-Basic").listedPrice(1500l).build())
                .build();
        Quotation quotation = quotationEngineClient.generateQuotation(req);

        // Assert response
        assertNotNull(quotation);
        assertNotNull(quotation.getQuotationCode());
    }
}
