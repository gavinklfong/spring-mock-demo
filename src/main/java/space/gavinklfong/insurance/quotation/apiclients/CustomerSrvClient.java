package space.gavinklfong.insurance.quotation.apiclients;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import space.gavinklfong.insurance.quotation.models.Customer;

import java.util.List;
import java.util.Optional;

@Component
public class CustomerSrvClient {

    private String customerSrvUrl;

    private WebClient webClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    public CustomerSrvClient(@Value("${app.customerSrvUrl}") String customerSrvUrl) {
        this.customerSrvUrl = customerSrvUrl;
        this.webClient = WebClient.builder().baseUrl(customerSrvUrl)
                .build();
    }

    public Optional<Customer> getCustomer(Long id)  {

        WebClient webClient = WebClient.create(customerSrvUrl);
        Mono<Customer> customer = webClient.get()
                .uri("/customers/" + id)
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals, clientResponse -> Mono.empty())
                .bodyToMono(Customer.class);

        return customer.blockOptional();
    }

    public List<Customer> getCustomers()  {

        WebClient webClient = WebClient.create(customerSrvUrl);
        Flux<Customer> customers = webClient.get()
                .uri("/customers")
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals, clientResponse -> Mono.empty())
                .bodyToFlux(Customer.class);

        return customers.collectList().block();
    }

}
