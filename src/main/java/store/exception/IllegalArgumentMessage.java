package store.exception;

public enum IllegalArgumentMessage implements ExceptionMessage {

    ;

    private final String message;

    IllegalArgumentMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
