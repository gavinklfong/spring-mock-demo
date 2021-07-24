package space.gavinklfong.insurance.quotation.apiclients;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.MediaType;
import org.mockserver.springtest.MockServerTest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import space.gavinklfong.insurance.quotation.models.Product;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@Slf4j
@MockServerTest("server.url=http://localhost:${mockServerPort}")
@ExtendWith(SpringExtension.class)
public class ProductSrvClientTests {

    @Value("${server.url}")
    private String serverUrl;

    private MockServerClient mockServerClient;

    @Test
    void givenRecordExists_getProduct() {

        final String PRODUCT_CODE = "CAR001-01";

        // Setup request matcher and response using MockServerClient API
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

        // Initialize API client and trigger request
        ProductSrvClient productSrvClient = new ProductSrvClient(serverUrl);
        Optional<Product> productOptional = productSrvClient.getProduct(PRODUCT_CODE);

        // Assert response
        assertTrue(productOptional.isPresent());
        Product product = productOptional.get();
        assertNotNull(product.getProductPlan());
        assertNotNull(product.getProductClass());
        assertTrue(product.getPostCodeInService().length > 0);
    }

    @Test
    void givenRecordExists_getProducts() {

        // Setup request matcher and response using OpenAPI definition
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

        // Initialize API client and trigger request
        ProductSrvClient productSrvClient = new ProductSrvClient(serverUrl);
        List<Product> products = productSrvClient.getProducts();

        // Assert response
        assertNotNull(products);
        assertTrue(products.size() > 0);
    }

    @Test
    void givenRecordNotFound_getProduct() {

        final String PRODUCT_CODE = "CAR001-01";

        // Setup request matcher and response using OpenAPI definition
        mockServerClient
                .when(
                        request()
                                .withMethod("GET")
                                .withPath("/products/.*")
                )
                .respond(
                        response()
                                .withStatusCode(404)
                );

        // Initialize API client and trigger request
        ProductSrvClient productSrvClient = new ProductSrvClient(serverUrl);
        Optional<Product> productOptional = productSrvClient.getProduct(PRODUCT_CODE);

        // Assert response
        assertTrue(productOptional.isEmpty());
    }

    @Test
    void givenRecordNotFound_getProducts() {

        // Setup request matcher and response using OpenAPI definition
        mockServerClient
                .when(
                        request()
                                .withMethod("GET")
                                .withPath("/products")
                )
                .respond(
                        response()
                                .withStatusCode(404)
                );

        // Initialize API client and trigger request
        ProductSrvClient productSrvClient = new ProductSrvClient(serverUrl);
        List<Product> products = productSrvClient.getProducts();

        // Assert response
        assertTrue(products.size() == 0);
    }
}
