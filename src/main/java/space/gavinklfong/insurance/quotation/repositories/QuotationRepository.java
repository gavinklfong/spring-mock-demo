package space.gavinklfong.insurance.quotation.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import space.gavinklfong.insurance.quotation.models.Quotation;

import java.util.Optional;

@Repository
public interface QuotationRepository extends CrudRepository<Quotation, String> {

}
