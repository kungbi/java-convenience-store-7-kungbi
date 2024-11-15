package store.exception;

public class ProductRepositoryException extends IllegalArgumentException {
    private static final String DEFAULT_MESSAGE = "상품 저장소 오류";

    public ProductRepositoryException() {
    }

    public ProductRepositoryException(ExceptionMessage message) {
        super(message.getMessage());
    }
}
