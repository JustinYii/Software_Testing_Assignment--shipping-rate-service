package shippingRateService;

public class InvalidShipmentException extends RuntimeException {
    public InvalidShipmentException(String message) {
        super(message);
    }
}
