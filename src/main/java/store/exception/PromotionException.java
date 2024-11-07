package store.exception;

import store.exception.message.ExceptionMessage;

public class PromotionException extends IllegalArgumentException {
    public PromotionException(ExceptionMessage message) {
        super(message.getMessage());
    }
}
