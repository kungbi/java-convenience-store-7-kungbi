package store.exception;

import store.exception.message.ExceptionMessage;

public class ProductInventoryException extends IllegalArgumentException {
    public ProductInventoryException(ExceptionMessage message) {
        super(message.getMessage());
    }

    public ProductInventoryException(ExceptionMessage message, Throwable throwable) {
        super(message.getMessage(), throwable);
    }
}
