package space.gavinklfong.insurance.quotation.apiclients;

import space.gavinklfong.insurance.quotation.models.Customer;

import java.io.IOException;
import java.util.Optional;

public interface CustomerSrvClient {

	Optional<Customer> getCustomer(Long id) throws IOException;
	
}
