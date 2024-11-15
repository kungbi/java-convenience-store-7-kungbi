package store.exception;

public class ProductCreationException extends IllegalArgumentException {
    private static final String DEFAULT_MESSAGE = "상품 생성 오류";

    public ProductCreationException() {
        super(DEFAULT_MESSAGE);
    }

    public ProductCreationException(ExceptionMessage message) {
        super(message.getMessage());
    }
}
