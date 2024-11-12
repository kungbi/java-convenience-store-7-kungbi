package store.exception;

import store.exception.message.ExceptionMessage;

public class DtoException extends IllegalArgumentException {
    public DtoException(ExceptionMessage message) {
        super(message.getMessage());
    }
}
