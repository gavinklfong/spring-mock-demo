package space.gavinklfong.insurance.quotation.mockserver;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.MediaType;

import java.util.Collections;

import static org.mockserver.mock.OpenAPIExpectation.openAPIExpectation;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@NoArgsConstructor
@AllArgsConstructor
public class MockServerExpectationInitializer {

    private MockServerClient mockServerClient;

    public void initializeForIntegrationTest() {
        initializeGenerateQuotation();
        initializeGetCustomers();
        initializeGetCustomer();
        initializeGetProduct();
        initializeGetProducts();
    }

    public void initializeGetCustomers() {
        mockServerClient
                .when(
                        request()
                                .withMethod("GET")
                                .withPath("/customers")
                )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withContentType(MediaType.APPLICATION_JSON)
                                .withBody(
                                        "[ " +
                                        "        {\"id\":1,\"name\":\"Alysson Witting\",\"dob\":\"2004-01-05\"},\n" +
                                        "        {\"id\":2,\"name\":\"Ramiro Barton\",\"dob\":\"1991-04-04\"},\n" +
                                        "        {\"id\":3,\"name\":\"Reagan Breitenberg\",\"dob\":\"1974-10-22\"},\n" +
                                        "        {\"id\":4,\"name\":\"Theodore Cartwright\",\"dob\":\"1997-10-25\"},\n" +
                                        "        {\"id\":5,\"name\":\"Una Maggio\",\"dob\":\"1995-04-03\"},\n" +
                                        "        {\"id\":6,\"name\":\"Krystina Gleason\",\"dob\":\"1985-08-10\"},\n" +
                                        "        {\"id\":7,\"name\":\"Cicero Grady\",\"dob\":\"1965-02-21\"},\n" +
                                        "        {\"id\":8,\"name\":\"Santino Gerlach\",\"dob\":\"1981-12-31\"},\n" +
                                        "        {\"id\":9,\"name\":\"Roman Wilderman\",\"dob\":\"1994-06-30\"},\n" +
                                        "        {\"id\":10,\"name\":\"Abe Greenholt\",\"dob\":\"1974-12-24\"}" +
                                                " ]"
                                )
                );
    }

    public void initializeGetCustomer() {
        mockServerClient
                .when(
                        request()
                                .withMethod("GET")
                                .withPath("/customers/.*")
                )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withContentType(MediaType.APPLICATION_JSON)
                                .withBody(
                                                "        {\"id\":1,\"name\":\"Alysson Witting\",\"dob\":\"1970-01-05\"}\n"
                                )
                );
    }

    public void initializeGetProduct() {
        mockServerClient
                .when(
                        request()
                                .withMethod("GET")
                                .withPath("/products/.*")
                )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withContentType(MediaType.APPLICATION_JSON)
                                .withBody(
                                        "{" +
                                                " \"productCode\": \"CAR001-01\"," +
                                                " \"productPlan\": \"Home-General\"," +
                                                " \"productClass\": \"Online\"," +
                                                " \"postCodeInService\": [" +
                                                " \"SW10\"," +
                                                " \"SW20\"," +
                                                " \"SW33\"" +
                                                " ]," +
                                                " \"listedPrice\": 100" +
                                                "}"
                                )
                );
    }

    public void initializeGetProducts() {
        mockServerClient
                .when(
                        request()
                                .withMethod("GET")
                                .withPath("/products")
                )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withContentType(MediaType.APPLICATION_JSON)
                                .withBody(
                                        "[" +
                                                "{" +
                                                " \"productCode\": \"CAR001-01\"," +
                                                " \"productPlan\": \"Home-General\"," +
                                                " \"productClass\": \"Online\"," +
                                                " \"postCodeInService\": [" +
                                                " \"SW10\"," +
                                                " \"SW20\"," +
                                                " \"SW33\"" +
                                                " ]," +
                                                " \"listedPrice\": 100" +
                                                "}," +
                                                "{" +
                                                " \"productCode\": \"CAR002-01\"," +
                                                " \"productPlan\": \"Home-Premier\"," +
                                                " \"productClass\": \"Online\"," +
                                                " \"postCodeInService\": [" +
                                                " \"E14\"," +
                                                " \"SW20\"," +
                                                " \"E01\"" +
                                                " ]," +
                                                " \"listedPrice\": 5000" +
                                                "}" +
                                                "]"
                                )
                );
    }

    public void initializeGenerateQuotation() {
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
    }

}
