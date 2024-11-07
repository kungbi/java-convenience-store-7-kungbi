package store.exception;

import store.exception.message.ExceptionMessage;

public class ProductException extends IllegalStateException {
    public ProductException(ExceptionMessage message) {
        super(message.getMessage());
    }

    public ProductException(ExceptionMessage message, Throwable cause) {
        super(message.getMessage(), cause);
    }
}
