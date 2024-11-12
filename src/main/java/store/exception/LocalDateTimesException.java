package store.exception;

import store.exception.message.ExceptionMessage;

public class LocalDateTimesException extends IllegalArgumentException {
    public LocalDateTimesException(ExceptionMessage message) {
        super(message.getMessage());
    }

    public LocalDateTimesException(ExceptionMessage message, Throwable cause) {
        super(message.getMessage(), cause);
    }
}
