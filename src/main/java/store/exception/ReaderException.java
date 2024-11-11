package store.exception;

import store.exception.message.ExceptionMessage;

public class ReaderException extends IllegalArgumentException {
    public ReaderException(ExceptionMessage message) {
        super(message.getMessage());
    }

    public ReaderException(ExceptionMessage message, Throwable cause) {
        super(message.getMessage(), cause);
    }
}
