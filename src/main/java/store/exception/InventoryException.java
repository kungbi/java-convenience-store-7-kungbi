package store.exception;

import store.exception.message.ExceptionMessage;

public class InventoryException extends IllegalArgumentException {
    public InventoryException(ExceptionMessage message) {
        super(message.getMessage());
    }

    public InventoryException(ExceptionMessage message, Throwable throwable) {
        super(message.getMessage(), throwable);
    }
}
