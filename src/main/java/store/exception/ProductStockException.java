package store.exception;

import store.exception.message.ExceptionMessage;

public class ProductStockException extends IllegalArgumentException {
    public ProductStockException(ExceptionMessage message) {
        super(message.getMessage());
    }

    public ProductStockException(ExceptionMessage message, Throwable throwable) {
        super(message.getMessage(), throwable);
    }
}
