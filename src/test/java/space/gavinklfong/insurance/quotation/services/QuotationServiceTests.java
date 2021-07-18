package space.gavinklfong.insurance.quotation.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import space.gavinklfong.insurance.quotation.apiclients.CustomerSrvClient;
import space.gavinklfong.insurance.quotation.apiclients.ProductSrvClient;
import space.gavinklfong.insurance.quotation.exceptions.QuotationCriteriaNotFulfilled;
import space.gavinklfong.insurance.quotation.models.Customer;
import space.gavinklfong.insurance.quotation.models.Product;
import space.gavinklfong.insurance.quotation.dtos.QuotationReq;
import space.gavinklfong.insurance.quotation.exceptions.RecordNotFoundException;
import space.gavinklfong.insurance.quotation.models.Quotation;
import space.gavinklfong.insurance.quotation.repositories.QuotationRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@Slf4j
@SpringJUnitConfig
@TestPropertySource(properties = {
        "app.quotation.expiryTime=1440"
})
@ContextConfiguration(classes = {QuotationService.class})
@Tag("UnitTest")
public class QuotationServiceTests {

    @MockBean
    private QuotationRepository quotationRepo;

    @MockBean
    private CustomerSrvClient customerSrvClient;

    @MockBean
    private ProductSrvClient productSrvClient;

    @Autowired
    private QuotationService quotationService;

    private Faker faker = new Faker();

    @Test
    void generateQuotation_belowThresholdAge() throws RecordNotFoundException, IOException, QuotationCriteriaNotFulfilled {

        final String PRODUCT_CODE = "CAR001-01";
        final long CUSTOMER_ID = 1l;
        final long LISTED_PRICE = 1500l;

        // Create mock response
        when(quotationRepo.save(any(Quotation.class))).thenAnswer(invocation -> {
            Quotation quotation = (Quotation) invocation.getArgument(0);
            return quotation.withQuotationCode(UUID.randomUUID().toString());
        });

        Optional<Customer> customer = Optional.of(
                Customer.builder()
                .id(CUSTOMER_ID)
                .dob(faker.date().birthday(18, 69)
                        .toInstant().atZone(ZoneId.systemDefault())
                        .toLocalDate())
                .name(faker.name().name())
                .build());
        when(customerSrvClient.getCustomer(anyLong())).thenReturn(customer);

        Optional<Product> product = Optional.of(Product.builder()
                .productCode(PRODUCT_CODE)
                .productClass("Online")
                .productPlan("Home-General")
                .buildingSumInsured(faker.number().randomNumber())
                .contentSumInsured(faker.number().randomNumber())
                .buildsAccidentalDamage("Optional")
                .contentsAccidentalDamage("Optional")
                .maxAlternativeAccoummodation(faker.number().randomNumber())
                .matchingItems(faker.bool().bool())
                .maxAlternativeAccoummodation(faker.number().randomNumber())
                .maxValuables(faker.number().randomNumber())
                .contentsInGarden(faker.number().randomNumber())
                .theftFromOutbuildings(faker.number().randomNumber())
                .customerAgeThreshold(70)
                .customerAgeThresholdAdjustmentRate(1.5)
                .postCodeInService(new String[] {"SW20", "SM1", "E12" })
                .postCodeDiscountRate(0.7)
                .listedPrice(LISTED_PRICE)
                .build());
        when(productSrvClient.getProduct(anyString())).thenReturn(product);

        // construct request
        QuotationReq req = QuotationReq.builder()
                .customerId(CUSTOMER_ID)
                .postCode("SW11")
                .productCode(PRODUCT_CODE)
                .build();

        // run test method
        Quotation result = quotationService.generateQuotation(req);
        assertEquals(LISTED_PRICE, result.getAmount());
        assertTrue(result.getExpiryTime().isAfter(LocalDateTime.now()));
        assertEquals(PRODUCT_CODE, result.getProductCode());
    }

    @Test
    void generateQuotation_aboveThresholdAge() throws RecordNotFoundException, IOException, QuotationCriteriaNotFulfilled {

        final String PRODUCT_CODE = "CAR001-01";
        final long CUSTOMER_ID = 1l;
        final long LISTED_PRICE = 1500l;

        // Create mock response
        when(quotationRepo.save(any(Quotation.class))).thenAnswer(invocation -> {
            Quotation quotation = (Quotation) invocation.getArgument(0);
            return quotation.withQuotationCode(UUID.randomUUID().toString());
        });

        Optional<Customer> customer = Optional.of(
                Customer.builder()
                        .id(CUSTOMER_ID)
                        .dob(faker.date().birthday(70, 99)
                                .toInstant().atZone(ZoneId.systemDefault())
                                .toLocalDate())
                        .name(faker.name().name())
                        .build()
        );
        when(customerSrvClient.getCustomer(anyLong())).thenReturn(customer);

        Optional<Product> product = Optional.of(Product.builder()
                .productCode(PRODUCT_CODE)
                .productClass("Online")
                .productPlan("Home-General")
                .buildingSumInsured(faker.number().randomNumber())
                .contentSumInsured(faker.number().randomNumber())
                .buildsAccidentalDamage("Optional")
                .contentsAccidentalDamage("Optional")
                .maxAlternativeAccoummodation(faker.number().randomNumber())
                .matchingItems(faker.bool().bool())
                .maxAlternativeAccoummodation(faker.number().randomNumber())
                .maxValuables(faker.number().randomNumber())
                .contentsInGarden(faker.number().randomNumber())
                .theftFromOutbuildings(faker.number().randomNumber())
                .customerAgeThreshold(70)
                .customerAgeThresholdAdjustmentRate(1.5)
                .postCodeInService(new String[] {"SW20", "SM1", "E12" })
                .postCodeDiscountRate(0.7)
                .listedPrice(LISTED_PRICE)
                .build());
        when(productSrvClient.getProduct(anyString())).thenReturn(product);

        // construct request
        QuotationReq req = QuotationReq.builder()
                .customerId(CUSTOMER_ID)
                .postCode("SW11")
                .productCode(PRODUCT_CODE)
                .build();

        // run test method
        Quotation result = quotationService.generateQuotation(req);
        assertTrue(result.getExpiryTime().isAfter(LocalDateTime.now()));
        assertEquals(PRODUCT_CODE, result.getProductCode());
    }

    @Test
    void generateQuotation_postCodeMatched() throws RecordNotFoundException, IOException, QuotationCriteriaNotFulfilled {

        final String PRODUCT_CODE = "CAR001-01";
        final long CUSTOMER_ID = 1l;
        final long LISTED_PRICE = 1500l;
        final String POST_CODE = "SW11";

        // Create mock response
        when(quotationRepo.save(any(Quotation.class))).thenAnswer(invocation -> {
            Quotation quotation = (Quotation) invocation.getArgument(0);
            return quotation.withQuotationCode(UUID.randomUUID().toString());
        });

        Optional<Customer> customer = Optional.of(
                Customer.builder()
                        .id(CUSTOMER_ID)
                        .dob(faker.date().birthday(18, 69)
                                .toInstant().atZone(ZoneId.systemDefault())
                                .toLocalDate())
                        .name(faker.name().name())
                        .build()
        );
        when(customerSrvClient.getCustomer(anyLong())).thenReturn(customer);

        Optional<Product> product = Optional.of(Product.builder()
                .productCode(PRODUCT_CODE)
                .productClass("Online")
                .productPlan("Home-General")
                .buildingSumInsured(faker.number().randomNumber())
                .contentSumInsured(faker.number().randomNumber())
                .buildsAccidentalDamage("Optional")
                .contentsAccidentalDamage("Optional")
                .maxAlternativeAccoummodation(faker.number().randomNumber())
                .matchingItems(faker.bool().bool())
                .maxAlternativeAccoummodation(faker.number().randomNumber())
                .maxValuables(faker.number().randomNumber())
                .contentsInGarden(faker.number().randomNumber())
                .theftFromOutbuildings(faker.number().randomNumber())
                .customerAgeThreshold(70)
                .customerAgeThresholdAdjustmentRate(1.5)
                .postCodeInService(new String[] {"SW20", "SM1", "E12", POST_CODE})
                .postCodeDiscountRate(0.7)
                .listedPrice(LISTED_PRICE)
                .build());
        when(productSrvClient.getProduct(anyString())).thenReturn(product);

        // construct request
        QuotationReq req = QuotationReq.builder()
                .customerId(CUSTOMER_ID)
                .postCode(POST_CODE)
                .productCode(PRODUCT_CODE)
                .build();

        // run test method
        Quotation result = quotationService.generateQuotation(req);
        assertTrue(result.getExpiryTime().isAfter(LocalDateTime.now()));
        assertEquals(PRODUCT_CODE, result.getProductCode());
    }

    @Test
    void generateQuotation_aboveThresholdAge_postCodeMatched() throws RecordNotFoundException, IOException, QuotationCriteriaNotFulfilled {

        final String PRODUCT_CODE = "CAR001-01";
        final long CUSTOMER_ID = 1l;
        final long LISTED_PRICE = 1500l;
        final String POST_CODE = "SW11";

        // Create mock response
        when(quotationRepo.save(any(Quotation.class))).thenAnswer(invocation -> {
            Quotation quotation = (Quotation) invocation.getArgument(0);
            return quotation.withQuotationCode(UUID.randomUUID().toString());
        });

        Optional<Customer> customer = Optional.of(
                Customer.builder()
                        .id(CUSTOMER_ID)
                        .dob(faker.date().birthday(70, 99)
                                .toInstant().atZone(ZoneId.systemDefault())
                                .toLocalDate())
                        .name(faker.name().name())
                        .build()
        );
        when(customerSrvClient.getCustomer(anyLong())).thenReturn(customer);

        Optional<Product> product = Optional.of(Product.builder()
                .productCode(PRODUCT_CODE)
                .productClass("Online")
                .productPlan("Home-General")
                .buildingSumInsured(faker.number().randomNumber())
                .contentSumInsured(faker.number().randomNumber())
                .buildsAccidentalDamage("Optional")
                .contentsAccidentalDamage("Optional")
                .maxAlternativeAccoummodation(faker.number().randomNumber())
                .matchingItems(faker.bool().bool())
                .maxAlternativeAccoummodation(faker.number().randomNumber())
                .maxValuables(faker.number().randomNumber())
                .contentsInGarden(faker.number().randomNumber())
                .theftFromOutbuildings(faker.number().randomNumber())
                .customerAgeThreshold(70)
                .customerAgeThresholdAdjustmentRate(1.5)
                .postCodeInService(new String[] {"SW20", "SM1", "E12", POST_CODE})
                .postCodeDiscountRate(0.7)
                .listedPrice(LISTED_PRICE)
                .build());
        when(productSrvClient.getProduct(anyString())).thenReturn(product);

        // construct request
        QuotationReq req = QuotationReq.builder()
                .customerId(CUSTOMER_ID)
                .postCode(POST_CODE)
                .productCode(PRODUCT_CODE)
                .build();

        // run test method
        Quotation result = quotationService.generateQuotation(req);
        assertTrue(result.getExpiryTime().isAfter(LocalDateTime.now()));
        assertEquals(PRODUCT_CODE, result.getProductCode());
    }
}
