package space.gavinklfong.insurance.quotation.apiclients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import space.gavinklfong.insurance.quotation.models.Product;

import java.util.Optional;

@Component
public class ProductSrvClientImpl implements ProductSrvClient {

    @Value("${app.productSrvUrl}")
    private String productSrvUrl;

    @Override
    public Optional<Product> getProduct(String id) {
        WebClient webClient = WebClient.create(productSrvUrl);
        Mono<Product> products = webClient.get()
                .uri("/products/" + id)
                .retrieve()
                .bodyToMono(Product.class);

        return products.blockOptional();
    }
}
