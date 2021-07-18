package space.gavinklfong.insurance.quotation.exceptions;

public class QuotationCriteriaNotFulfilledException extends Exception {

    public QuotationCriteriaNotFulfilledException() {
        super();
    }

    public QuotationCriteriaNotFulfilledException(String msg) {
        super(msg);
    }
}
