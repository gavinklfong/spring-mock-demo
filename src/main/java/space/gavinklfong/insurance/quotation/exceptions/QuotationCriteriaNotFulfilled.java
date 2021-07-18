package space.gavinklfong.insurance.quotation.exceptions;

public class QuotationCriteriaNotFulfilled extends Exception {

    public QuotationCriteriaNotFulfilled() {
        super();
    }

    public QuotationCriteriaNotFulfilled(String msg) {
        super(msg);
    }
}
