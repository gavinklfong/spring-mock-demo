package space.gavinklfong.insurance.quotation.apiclients;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import space.gavinklfong.insurance.quotation.models.Customer;

import java.io.IOException;
import java.util.Optional;

@Component
public class CustomerSrvClientImpl implements CustomerSrvClient {

    @Value("${app.customerSrvUrl}")
    private String customerSrvUrl;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Optional<Customer> getCustomer(Long id) throws IOException {

        WebClient webClient = WebClient.create(customerSrvUrl);
        Mono<Customer> customer = webClient.get()
                .uri("/customers/" + id)
                .retrieve()
                .bodyToMono(Customer.class);

        return customer.blockOptional();
    }

}
