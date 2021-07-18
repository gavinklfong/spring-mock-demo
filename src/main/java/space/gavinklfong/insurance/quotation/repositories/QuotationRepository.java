package space.gavinklfong.insurance.quotation.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import space.gavinklfong.insurance.quotation.models.Quotation;

@Repository
public interface QuotationRepository extends CrudRepository<Quotation, String> {

}
