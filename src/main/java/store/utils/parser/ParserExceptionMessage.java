package store.utils.parser;

public enum ParserExceptionMessage {
    INVALID_PROMOTION_DATA("Invalid promotion data"),
    ;

    private final String message;

    ParserExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
