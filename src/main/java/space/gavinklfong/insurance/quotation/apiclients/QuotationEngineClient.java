package space.gavinklfong.insurance.quotation.apiclients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import space.gavinklfong.insurance.quotation.dtos.QuotationEngineReq;
import space.gavinklfong.insurance.quotation.models.Quotation;

public class QuotationEngineClient {

    @Value("${app.quotationEngineSrvUrl}")
    private String quotationEngineSrvUrl;

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
