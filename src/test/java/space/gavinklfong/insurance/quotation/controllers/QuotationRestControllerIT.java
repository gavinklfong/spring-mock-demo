package space.gavinklfong.insurance.quotation.controllers;

import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.springtest.MockServerTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
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

import java.time.Duration;

@Slf4j
@Tag("IntegrationTest")
public class QuotationRestControllerIT {

	WebTestClient webTestClient;

	@BeforeEach
	void setUp() {
		String port = System.getProperty("test.server.port");
		webTestClient = WebTestClient.bindToServer().baseUrl("http://localhost:" + port)
				.responseTimeout(Duration.ofSeconds(15))
				.build();
	}

	@Test
	public void givenEverythingPassed_generateQuotation() throws Exception {

		// fire request to quotation and verify the response
		QuotationReq quotationReq = QuotationReq.builder()
				.postCode("SW20")
				.customerId(1l)
				.productCode("CAR001-01")
				.build();

		webTestClient.post()
				.uri("/quotations/generate")
				.contentType(MediaType.APPLICATION_JSON)
				.body(Mono.just(quotationReq), QuotationReq.class)
				.exchange()
				.expectStatus().isOk()
				.expectBody(Quotation.class);
	}

}
