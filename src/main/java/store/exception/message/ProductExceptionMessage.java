package store.exception.message;

public enum ProductExceptionMessage implements ExceptionMessage {

    ;

    private final String message;

    ProductExceptionMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
