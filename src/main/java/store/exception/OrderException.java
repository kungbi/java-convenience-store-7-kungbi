package store.exception;

public class OrderException extends IllegalArgumentException {
    public OrderException(String message) {
        super(message);
    }
}
