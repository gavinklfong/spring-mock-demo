package space.gavinklfong.insurance.quotation.apiclients;

public class ServiceNotAvailableException extends RuntimeException {

    public ServiceNotAvailableException() {
        super();
    }

    public ServiceNotAvailableException(String name) {
        super(name);
    }
}
