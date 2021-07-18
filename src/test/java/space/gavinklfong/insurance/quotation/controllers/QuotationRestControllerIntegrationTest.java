package space.gavinklfong.insurance.quotation.controllers;

import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.springtest.MockServerTest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import space.gavinklfong.insurance.quotation.apiclients.CustomerSrvClient;
import space.gavinklfong.insurance.quotation.apiclients.ProductSrvClient;
import space.gavinklfong.insurance.quotation.apiclients.QuotationEngineClient;
import space.gavinklfong.insurance.quotation.dtos.QuotationReq;
import space.gavinklfong.insurance.quotation.models.Quotation;

@Slf4j
@MockServerTest("server.url=http://localhost:${mockServerPort}")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integrationtest")
@Tag("IntegrationTest")
public class QuotationRestControllerIntegrationTest {
	
	// Configure forex rate api client to point to mock api server
    @TestConfiguration
    static class TestContextConfiguration {
    	@Bean
    	@Primary
    	public CustomerSrvClient customerSrvClient(@Value("${customerSrvUrl.url}") String url) {
    		return new CustomerSrvClient(url);
    	}

		@Bean
		@Primary
		public ProductSrvClient productSrvClient(@Value("${productSrvUrl.url}") String url) {
			return new ProductSrvClient(url);
		}

		@Bean
		@Primary
		public QuotationEngineClient quotationEngineClient(@Value("${quotationEngineSrvUrl.url}") String url) {
			return new QuotationEngineClient(url);
		}
    }

	private Faker faker = new Faker();

	private MockServerClient mockServerClient;
	
	WebTestClient webTestClient;

	@LocalServerPort
	private int port;

	@BeforeEach
	void setUp() {
		webTestClient = WebTestClient.bindToServer().baseUrl("http://localhost:" + port).build();
	}

	@Test
	public void givenEverythingPassed_generateQuotation() throws Exception {

		// fire request to book rate and verify the response
		QuotationReq quotationReq = QuotationReq.builder()
				.postCode("SW20")
				.customerId(1l)
				.productCode("CAR01-001")
				.build();

		webTestClient.post()
				.uri("/quotations/generate")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.body(Mono.just(quotationReq), QuotationReq.class)
				.exchange()
				.expectStatus().isOk()
				.expectBody(Quotation.class);
	}

//	@Test
//	public void givenQuotationExists_fetchQuotation() throws Exception {
//
//		final String QUOTATION_CODE = "e2cfdbe6-04f5-46e0-a3ec-eb176a1528be";
//
//		webTestClient.get()
//		.uri(uriBuilder -> uriBuilder
//				.path("/quotations/" + QUOTATION_CODE)
//				.build()
//				)
//		.accept(MediaType.APPLICATION_JSON)
//		.exchange()
//		.expectStatus().isOk();
//	}
	
}
