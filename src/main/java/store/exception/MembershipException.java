package store.exception;

import store.exception.message.ExceptionMessage;

public class MembershipException extends IllegalArgumentException {
    public MembershipException(ExceptionMessage message) {
        super(message.getMessage());
    }

    public MembershipException(ExceptionMessage message, Throwable cause) {
        super(message.getMessage(), cause);
    }
}
