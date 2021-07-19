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
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {QuotationRestController.class})
public class QuotationRestControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuotationService quotationService;

    private Faker faker = new Faker();

    @Test
    void fetchQuotation() throws Exception {

        final String QUOTATION_CODE = "e2cfdbe6-04f5-46e0-a3ec-eb176a1528be";

        when(quotationService.fetchQuotation(anyString()))
                .thenAnswer(invocation -> {
                            String quotationCode = (String) invocation.getArgument(0);

                            return Optional.of(Quotation.builder()
                                    .quotationCode(quotationCode)
                                    .productCode("CAR001-004")
                                    .amount(faker.number().randomDouble(2, 1000, 5000))
                                    .expiryTime(LocalDateTime.now().plusDays(2))
                                    .customerId(1l)
                                    .build());
                        }
                );

        mockMvc.perform(
                get("/quotations/" + QUOTATION_CODE)
        )
                .andDo((print()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quotationCode").value(QUOTATION_CODE));

    }

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
                post("/quotations/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req))
        )
                .andDo((print()))
                .andExpect(status().isOk());

    }

}
