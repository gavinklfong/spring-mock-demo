package space.gavinklfong.insurance.quotation.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import space.gavinklfong.insurance.quotation.dtos.QuotationReq;
import space.gavinklfong.insurance.quotation.exceptions.QuotationCriteriaNotFulfilledException;
import space.gavinklfong.insurance.quotation.exceptions.RecordNotFoundException;
import space.gavinklfong.insurance.quotation.models.Quotation;
import space.gavinklfong.insurance.quotation.services.QuotationService;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/quotations")
public class QuotationRestController {

	@Autowired
	private QuotationService quotationService;
	
	@GetMapping(value= {"/{id}"}, produces=MediaType.APPLICATION_JSON_VALUE)
	public Quotation getQuotation(@PathVariable String id) {

		Optional<Quotation> quotation = quotationService.fetchQuotation(id);
		if (quotation.isPresent()) {
			return quotation.get();
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Quotation record not found");
		}
	}
	
	
	@PostMapping(consumes=MediaType.APPLICATION_JSON_VALUE,
				produces=MediaType.APPLICATION_JSON_VALUE)
	public Quotation generateQuotation(@Valid @RequestBody QuotationReq req) throws IOException, RecordNotFoundException, QuotationCriteriaNotFulfilledException {
		return quotationService.generateQuotation(req);
	}
	
	
	
}
