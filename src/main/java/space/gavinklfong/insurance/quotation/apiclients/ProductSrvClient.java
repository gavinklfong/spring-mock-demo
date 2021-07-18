package space.gavinklfong.insurance.quotation.apiclients;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import space.gavinklfong.insurance.quotation.models.Product;

import java.util.List;
import java.util.Optional;

@Component
public class ProductSrvClient {

    private String productSrvUrl;

    private WebClient webClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    public ProductSrvClient(@Value("${app.productSrvUrl}") String productSrvUrl) {
        this.productSrvUrl = productSrvUrl;
        this.webClient = WebClient.builder().baseUrl(productSrvUrl)
                .build();
    }

    public Optional<Product> getProduct(String id) {
        WebClient webClient = WebClient.create(productSrvUrl);
        Mono<Product> products = webClient.get()
                .uri("/products/" + id)
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals, clientResponse -> Mono.empty())
                .bodyToMono(Product.class);

        return products.blockOptional();
    }

    public List<Product> getProducts()  {

        WebClient webClient = WebClient.create(productSrvUrl);
        Flux<Product> products = webClient.get()
                .uri("/products")
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals, clientResponse -> Mono.empty())
                .bodyToFlux(Product.class);

        return products.collectList().block();
    }
}
