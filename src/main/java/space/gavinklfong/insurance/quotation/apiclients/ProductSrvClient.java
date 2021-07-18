package space.gavinklfong.insurance.quotation.apiclients;

import space.gavinklfong.insurance.quotation.models.Product;

import java.util.Optional;

public interface ProductSrvClient {
	Optional<Product> getProduct(String id);
	
}
