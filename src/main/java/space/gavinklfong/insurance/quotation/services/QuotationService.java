package space.gavinklfong.insurance.quotation.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@Service
public class QuotationService {

	public static final int CUSTOMER_ELIGIBLE_AGE = 18;

	@Autowired
	private QuotationRepository quotationRepo;

	@Autowired
	private QuotationEngineClient quotationEngineClient;

	@Autowired
	private CustomerSrvClient customerSrvClient;	
	
	@Autowired
	private ProductSrvClient productSrvClient;

	public Quotation generateQuotation(QuotationReq request) throws IOException, RecordNotFoundException, QuotationCriteriaNotFulfilledException {
		
		// get customer info
		Optional<Customer> customerOptional = customerSrvClient.getCustomer(request.getCustomerId());
		Customer customer = customerOptional.orElseThrow(() -> new RecordNotFoundException("Unknown customer"));

		// customer's age should be 18 or above
		LocalDateTime now = LocalDateTime.now();
		Period period = Period.between(customer.getDob(), now.toLocalDate());
		if (period.getYears() < CUSTOMER_ELIGIBLE_AGE) {
			throw new QuotationCriteriaNotFulfilledException("customer's age < 18");
		}

		// get product spec
		Optional<Product> productOptional = productSrvClient.getProduct(request.getProductCode());
		Product product = productOptional.orElseThrow(() -> new RecordNotFoundException("Unknown product"));

		// the request post code should be within the product's service scope
		if (!Stream.of(product.getPostCodeInService()).anyMatch(s -> s.equalsIgnoreCase(request.getPostCode()))) {
			throw new QuotationCriteriaNotFulfilledException(String.format("Request post code %s is not within the scope of service", request.getPostCode()));
		}

		QuotationEngineReq quotationEngineReq = new QuotationEngineReq(customer, product);
		Quotation quotation = quotationEngineClient.generateQuotation(quotationEngineReq);

		quotationRepo.save(quotation);

		return quotation;
		
	}

	public Optional<Quotation> fetchQuotation(String quotationCode) {
		return quotationRepo.findById(quotationCode);
	}

}
