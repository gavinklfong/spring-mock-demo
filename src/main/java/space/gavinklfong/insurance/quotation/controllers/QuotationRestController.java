package space.gavinklfong.insurance.quotation.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import space.gavinklfong.insurance.quotation.dtos.QuotationReq;
import space.gavinklfong.insurance.quotation.exceptions.QuotationCriteriaNotFulfilledException;
import space.gavinklfong.insurance.quotation.exceptions.RecordNotFoundException;
import space.gavinklfong.insurance.quotation.models.Quotation;
import space.gavinklfong.insurance.quotation.services.QuotationService;

import javax.validation.Valid;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/quotations")
public class QuotationRestController {

	@Autowired
	private QuotationService quotationService;
	
//	@GetMapping(value= {"/{id}"}, produces=MediaType.APPLICATION_JSON_VALUE)
//	public Quotation getQuotation(@PathVariable String id) {
//
//		Optional<Quotation> product = quotationService.retrieveQuotation(id);
//		if (product.isPresent()) {
//			return product.get();
//		} else {
//			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Quotation record not found");
//		}
//	}
	
	
	@PostMapping(consumes=MediaType.APPLICATION_JSON_VALUE,
				produces=MediaType.APPLICATION_JSON_VALUE)
	public Quotation generateQuotation(@Valid @RequestBody QuotationReq req) throws IOException, RecordNotFoundException, QuotationCriteriaNotFulfilledException {
		
		log.debug(req.toString());
		
		return quotationService.generateQuotation(req);
	}
	
	
	
}
