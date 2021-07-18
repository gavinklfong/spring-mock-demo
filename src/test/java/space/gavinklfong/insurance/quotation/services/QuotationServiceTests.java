package space.gavinklfong.insurance.quotation.services;

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
import space.gavinklfong.insurance.quotation.apiclients.QuotationEngineClient;
import space.gavinklfong.insurance.quotation.dtos.QuotationEngineReq;
import space.gavinklfong.insurance.quotation.dtos.QuotationReq;
import space.gavinklfong.insurance.quotation.exceptions.QuotationCriteriaNotFulfilledException;
import space.gavinklfong.insurance.quotation.exceptions.RecordNotFoundException;
import space.gavinklfong.insurance.quotation.models.Customer;
import space.gavinklfong.insurance.quotation.models.Product;
import space.gavinklfong.insurance.quotation.models.Quotation;
import space.gavinklfong.insurance.quotation.repositories.QuotationRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
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

    @MockBean
    private QuotationEngineClient quotationEngineClient;

    @Autowired
    private QuotationService quotationService;

    private Faker faker = new Faker();


    @Test
    void givenEverythingPassed_requestForQuotation() throws IOException, RecordNotFoundException, QuotationCriteriaNotFulfilledException {

        final String PRODUCT_CODE = "CAR001-01";
        final long CUSTOMER_ID = 1l;
        final long LISTED_PRICE = 1500l;
        final String POST_CODE = "SW20";
        final String[] PRODUCT_POST_CODES = {POST_CODE, "SM1", "E12"};
        final double QUOTATION_AMOUNT = 1500;

        setupQuotationRepo();
        setupCustomerSrvClient(CUSTOMER_ID, faker.date().birthday(18, 99)
                .toInstant().atZone(ZoneId.systemDefault())
                .toLocalDate());
        setupProductSrvClient(PRODUCT_CODE, PRODUCT_POST_CODES, LISTED_PRICE);
        setupQuotationEngineClient(QUOTATION_AMOUNT);

        QuotationReq req = QuotationReq.builder()
                .customerId(CUSTOMER_ID)
                .productCode(PRODUCT_CODE)
                .postCode(POST_CODE)
                .build();
        Quotation quotation = quotationService.generateQuotation(req);

        assertEquals(QUOTATION_AMOUNT, quotation.getAmount());
        assertNotNull(quotation.getQuotationCode());
        assertTrue(quotation.getExpiryTime().isAfter(LocalDateTime.now()));
        assertEquals(CUSTOMER_ID, quotation.getCustomerId());
        assertEquals(PRODUCT_CODE, quotation.getProductCode());
    }

    @Test
    void givenCustomerBelow18_requestForQuotation() throws IOException, RecordNotFoundException, QuotationCriteriaNotFulfilledException {

        final String PRODUCT_CODE = "CAR001-01";
        final long CUSTOMER_ID = 1l;
        final long LISTED_PRICE = 1500l;
        final String POST_CODE = "SW20";
        final String[] PRODUCT_POST_CODES = {POST_CODE, "SM1", "E12"};
        final double QUOTATION_AMOUNT = 1500;

        setupQuotationRepo();
        setupCustomerSrvClient(CUSTOMER_ID, faker.date().birthday(0, 17)
                .toInstant().atZone(ZoneId.systemDefault())
                .toLocalDate());
        setupProductSrvClient(PRODUCT_CODE, PRODUCT_POST_CODES, LISTED_PRICE);
        setupQuotationEngineClient(QUOTATION_AMOUNT);

        QuotationReq req = QuotationReq.builder()
                .customerId(CUSTOMER_ID)
                .productCode(PRODUCT_CODE)
                .postCode(POST_CODE)
                .build();
        assertThrows(QuotationCriteriaNotFulfilledException.class, () -> {
            quotationService.generateQuotation(req);
        });
    }

    @Test
    void givenCustomerAbove18_postCodeOutOfScope_requestForQuotation() throws IOException, RecordNotFoundException, QuotationCriteriaNotFulfilledException {

        final String PRODUCT_CODE = "CAR001-01";
        final long CUSTOMER_ID = 1l;
        final long LISTED_PRICE = 1500l;
        final String POST_CODE = "SW20";
        final String[] PRODUCT_POST_CODES = {"SM1", "E12"};
        final double QUOTATION_AMOUNT = 1500;

        setupQuotationRepo();
        setupCustomerSrvClient(CUSTOMER_ID, faker.date().birthday(18, 99)
                .toInstant().atZone(ZoneId.systemDefault())
                .toLocalDate());
        setupProductSrvClient(PRODUCT_CODE, PRODUCT_POST_CODES, LISTED_PRICE);
        setupQuotationEngineClient(QUOTATION_AMOUNT);

        QuotationReq req = QuotationReq.builder()
                .customerId(CUSTOMER_ID)
                .productCode(PRODUCT_CODE)
                .postCode(POST_CODE)
                .build();

        assertThrows(QuotationCriteriaNotFulfilledException.class, () -> {
            quotationService.generateQuotation(req);
        });

    }

    @Test
    void givenCustomerBelow18_postCodeOutOfScope_requestForQuotation() throws IOException, RecordNotFoundException, QuotationCriteriaNotFulfilledException {

        final String PRODUCT_CODE = "CAR001-01";
        final long CUSTOMER_ID = 1l;
        final long LISTED_PRICE = 1500l;
        final String POST_CODE = "SW20";
        final String[] PRODUCT_POST_CODES = {"SM1", "E12"};
        final double QUOTATION_AMOUNT = 1500;

        setupQuotationRepo();
        setupCustomerSrvClient(CUSTOMER_ID, faker.date().birthday(0, 17)
                .toInstant().atZone(ZoneId.systemDefault())
                .toLocalDate());
        setupProductSrvClient(PRODUCT_CODE, PRODUCT_POST_CODES, LISTED_PRICE);
        setupQuotationEngineClient(QUOTATION_AMOUNT);

        QuotationReq req = QuotationReq.builder()
                .customerId(CUSTOMER_ID)
                .productCode(PRODUCT_CODE)
                .postCode(POST_CODE)
                .build();
        assertThrows(QuotationCriteriaNotFulfilledException.class, () -> {
            quotationService.generateQuotation(req);
        });
    }

    @Test
    void givenUnknownCustomer_requestForQuotation() throws IOException, RecordNotFoundException, QuotationCriteriaNotFulfilledException {

        final String PRODUCT_CODE = "CAR001-01";
        final long CUSTOMER_ID = 1l;
        final long LISTED_PRICE = 1500l;
        final String POST_CODE = "SW20";
        final String[] PRODUCT_POST_CODES = {"SM1", "E12"};
        final double QUOTATION_AMOUNT = 1500;

        setupQuotationRepo();
        setupProductSrvClient(PRODUCT_CODE, PRODUCT_POST_CODES, LISTED_PRICE);
        setupQuotationEngineClient(QUOTATION_AMOUNT);

        when(customerSrvClient.getCustomer(anyLong())).thenReturn(Optional.empty());

        QuotationReq req = QuotationReq.builder()
                .customerId(CUSTOMER_ID)
                .productCode(PRODUCT_CODE)
                .postCode(POST_CODE)
                .build();
        assertThrows(RecordNotFoundException.class, () -> {
            quotationService.generateQuotation(req);
        });
    }

    @Test
    void givenUnknownProduct_requestForQuotation() throws IOException, RecordNotFoundException, QuotationCriteriaNotFulfilledException {

        final String PRODUCT_CODE = "CAR001-01";
        final long CUSTOMER_ID = 1l;
        final long LISTED_PRICE = 1500l;
        final String POST_CODE = "SW20";
        final String[] PRODUCT_POST_CODES = {"SM1", "E12"};
        final double QUOTATION_AMOUNT = 1500;

        setupQuotationRepo();
        setupQuotationEngineClient(QUOTATION_AMOUNT);
        setupCustomerSrvClient(CUSTOMER_ID, faker.date().birthday(18, 99)
                .toInstant().atZone(ZoneId.systemDefault())
                .toLocalDate());

        when(productSrvClient.getProduct(anyString())).thenReturn(Optional.empty());

        QuotationReq req = QuotationReq.builder()
                .customerId(CUSTOMER_ID)
                .productCode(PRODUCT_CODE)
                .postCode(POST_CODE)
                .build();
        assertThrows(RecordNotFoundException.class, () -> {
            quotationService.generateQuotation(req);
        });
    }

    void setupCustomerSrvClient(Long customerId, LocalDate dob) throws IOException {

        Optional<Customer> customer = Optional.of(Customer.builder()
                .id(customerId)
                .dob(dob)
                .name(faker.name().name())
                .build());

        when(customerSrvClient.getCustomer(anyLong())).thenReturn(customer);
    }

    void setupProductSrvClient(String productCode, String[] postCodes, Long listedPrice) {
        Optional<Product> product = Optional.of(Product.builder()
                .productCode(productCode)
                .productClass("Online")
                .productPlan("Home-General")
                .postCodeInService(postCodes)
                .listedPrice(listedPrice)
                .build());

        when(productSrvClient.getProduct(anyString())).thenReturn(product);
    }

    void setupQuotationEngineClient(Double quotationAmount) {
        when(quotationEngineClient.generateQuotation(any(QuotationEngineReq.class))).thenAnswer(invocation -> {
            QuotationEngineReq req = (QuotationEngineReq) invocation.getArgument(0);
            return Quotation.builder()
                    .expiryTime(faker.date().future(2, TimeUnit.DAYS)
                            .toInstant().atZone(ZoneId.systemDefault())
                            .toLocalDateTime())
                    .amount(quotationAmount)
                    .productCode(req.getProduct().getProductCode())
                    .customerId(req.getCustomer().getId())
                    .quotationCode(UUID.randomUUID().toString())
                    .build();
        });
    }

    void setupQuotationRepo() {
        when(quotationRepo.save(any(Quotation.class))).thenAnswer(invocation -> {
            return (Quotation) invocation.getArgument(0);
        });
    }
}
