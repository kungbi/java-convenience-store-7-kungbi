package store.exception;

import store.exception.message.ExceptionMessage;

public class PurchaseException extends IllegalArgumentException {
    public PurchaseException(ExceptionMessage message) {
        super(message.getMessage());
    }
}
