package store.exception;

public enum IllegalArgumentMessage implements ExceptionMessage {
    PRODUCT_DUPLICATE("상품 중복");

    private final String message;

    IllegalArgumentMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
