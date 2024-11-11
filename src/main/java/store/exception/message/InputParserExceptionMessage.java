package store.exception.message;

public enum InputParserExceptionMessage implements ExceptionMessage {
    NULL_OR_BLANK("NULL 또는 빈 문자열입니다."),
    NUMBER_FORMAT_OR_OVERFLOW_EXCEPTION("숫자만 입력해주세요. (혹은 숫자 범위를 벗어났습니다.)"),
    INVALID_FORMAT("올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요."),
    PURCHASE_ITEMS_NULL_OR_EMPTY("구매할 상품이 없습니다. 다시 입력해 주세요.")
    ;

    private final String message;

    InputParserExceptionMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
