package space.gavinklfong.insurance.quotation.apiclients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import space.gavinklfong.insurance.quotation.dtos.QuotationEngineReq;
import space.gavinklfong.insurance.quotation.models.Quotation;

@Component
public class QuotationEngineClient {

    private String quotationEngineSrvUrl;

    private WebClient webClient;

    @Autowired
    public QuotationEngineClient(@Value("${app.quotationEngineSrvUrl}") String quotationEngineSrvUrl) {
        this.quotationEngineSrvUrl = quotationEngineSrvUrl;
        this.webClient = WebClient.builder().baseUrl(quotationEngineSrvUrl)
                .build();
    }

    public Quotation generateQuotation(QuotationEngineReq req) {
        WebClient webClient = WebClient.create(quotationEngineSrvUrl);
        Mono<Quotation> quotation = webClient.post()
                .uri("/quotation/generate")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(req), QuotationEngineReq.class)
                .retrieve()
                .bodyToMono(Quotation.class);

        return quotation.block();
    }

}
