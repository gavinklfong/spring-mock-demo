package space.gavinklfong.insurance.quotation.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionAdvice {

    @ExceptionHandler({QuotationCriteriaNotFulfilledException.class})
    public final ResponseEntity<String> handleQuotationException(Exception ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler({RecordNotFoundException.class})
    public final ResponseEntity<String> handleRecordNotFoundException(Exception ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
