package space.gavinklfong.insurance.quotation.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import space.gavinklfong.insurance.quotation.dtos.QuotationReq;
import space.gavinklfong.insurance.quotation.models.Quotation;
import space.gavinklfong.insurance.quotation.services.QuotationService;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {QuotationRestController.class})
public class QuotationRestControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuotationService quotationService;

    private Faker faker = new Faker();

    @Test
    void generateQuotation() throws Exception {

        when(quotationService.generateQuotation(any(QuotationReq.class)))
                .thenAnswer(invocation -> {
                    QuotationReq req = (QuotationReq) invocation.getArgument(0);

                    return Quotation.builder()
                            .quotationCode(UUID.randomUUID().toString())
                            .productCode(req.getProductCode())
                            .amount(faker.number().randomDouble(2, 1000, 5000))
                            .expiryTime(LocalDateTime.now().plusMinutes(10))
                            .build();
                }
        );

        QuotationReq req = QuotationReq.builder()
                .postCode(faker.address().zipCode())
                .customerId(faker.number().randomNumber())
                .productCode(faker.code().toString())
                .build();

        ObjectMapper mapper = new ObjectMapper();

        mockMvc.perform(
                post("/quotations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req))
        )
                .andDo((print()))
                .andExpect(status().isOk());

    }

}
