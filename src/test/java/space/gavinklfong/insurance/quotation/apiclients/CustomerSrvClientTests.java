package space.gavinklfong.insurance.quotation.apiclients;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.client.MockServerClient;
import org.mockserver.springtest.MockServerTest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import space.gavinklfong.insurance.quotation.models.Customer;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockserver.mock.OpenAPIExpectation.openAPIExpectation;

@Slf4j
@MockServerTest("server.url=http://localhost:${mockServerPort}")
@ExtendWith(SpringExtension.class)
public class CustomerSrvClientTests {

    @Value("${server.url}")
    private String serverUrl;

    private MockServerClient mockServerClient;

    @Test
    void givenRecordExists_getCustomer() {

        // Setup request matcher and response using OpenAPI definition
        mockServerClient
                .upsert(
                        openAPIExpectation("mockapi/customerSrv.json")
                                .withOperationsAndResponses(Collections.singletonMap("getCustomerById", "200"))
                );

        // Initialize API client and trigger request
        CustomerSrvClient customerSrvClient = new CustomerSrvClient(serverUrl);
        Optional<Customer> customerOptional = customerSrvClient.getCustomer(1l);

        // Assert response
        assertTrue(customerOptional.isPresent());
        Customer customer = customerOptional.get();
        assertNotNull(customer.getDob());
        assertNotNull(customer.getName());
    }

    @Test
    void givenRecordNotFound_getCustomer() {

        // Setup request matcher and response using OpenAPI definition
        mockServerClient
                .upsert(
                        openAPIExpectation("mockapi/customerSrv.json")
                                .withOperationsAndResponses(Collections.singletonMap("getCustomerById", "404"))
                );

        // Initialize API client and trigger request
        CustomerSrvClient customerSrvClient = new CustomerSrvClient(serverUrl);
        Optional<Customer> customerOptional = customerSrvClient.getCustomer(1l);

        // Assert response
        assertTrue(customerOptional.isEmpty());
    }

    @Test
    void givenRecordExists_getCustomers() {

        // Setup request matcher and response using OpenAPI definition
        mockServerClient
                .upsert(
                        openAPIExpectation("mockapi/customerSrv.json")
                                .withOperationsAndResponses(Collections.singletonMap("getCustomers", "200"))
                );

        // Initialize API client and trigger request
        CustomerSrvClient customerSrvClient = new CustomerSrvClient(serverUrl);
        List<Customer> customers = customerSrvClient.getCustomers();

        // Assert response
        assertTrue(customers.size() > 0);
    }

    @Test
    void givenRecordNotFound_getCustomers() {

        // Setup request matcher and response using OpenAPI definition
        mockServerClient
                .upsert(
                        openAPIExpectation("mockapi/customerSrv.json")
                                .withOperationsAndResponses(Collections.singletonMap("getCustomers", "404"))
                );

        // Initialize API client and trigger request
        CustomerSrvClient customerSrvClient = new CustomerSrvClient(serverUrl);
        List<Customer> customers = customerSrvClient.getCustomers();

        // Assert response
        assertTrue(customers.size() == 0);
    }
}
