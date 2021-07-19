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
import space.gavinklfong.insurance.quotation.models.Quotation;

import java.util.List;
import java.util.Optional;

@Component
public class QuotationRepositoryClient {

    private String quotationRepoUrl;

    private WebClient webClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    public QuotationRepositoryClient(@Value("${app.quotationRepoUrl}") String customerSrvUrl) {
        this.quotationRepoUrl = quotationRepoUrl;
        this.webClient = WebClient.builder().baseUrl(quotationRepoUrl)
                .build();
    }

    public Optional<Quotation> getQuotations(String quotatoinCode)  {

        WebClient webClient = WebClient.create(quotationRepoUrl);
        Mono<Quotation> quotation = webClient.get()
                .uri("/quotations/" + quotatoinCode)
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals, clientResponse -> Mono.empty())
                .bodyToMono(Quotation.class);

        return quotation.blockOptional();
    }

    public List<Quotation> getQuotations()  {

        WebClient webClient = WebClient.create(quotationRepoUrl);
        Flux<Quotation> quotations = webClient.get()
                .uri("/quotations")
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals, clientResponse -> Mono.empty())
                .bodyToFlux(Quotation.class);

        return quotations.collectList().block();
    }

}
