package store.exception;

import store.exception.message.ExceptionMessage;

public class InputParserException extends IllegalArgumentException {
    public InputParserException(ExceptionMessage message) {
        super(message.getMessage());
    }

    public InputParserException(ExceptionMessage message, Throwable cause) {
        super(message.getMessage(), cause);
    }
}
