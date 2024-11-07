package store.exception.message;

public enum PromotionExceptionMessage implements ExceptionMessage {
    NULL_NAME("프로모션 이름이 NULL 입니다."),
    EMPTY_NAME("프로모션 이름이 빈 문자열입니다."),
    EXCEED_NAME_LENGTH("프로모션 이름이 50자를 초과했습니다."),
    NULL_DATE("프로모션 날짜에 NULL 값이 들어갔습니다."),

    BUY_QUANTITY_MIN("구매 수량은 1개 이상이어야 합니다."),
    FREE_QUANTITY_MIN("무료 수량은 1개 이상이어야 합니다."),

    DATE_ORDER("프로모션 날짜가 잘못되었습니다."),
    ;

    private final String message;

    PromotionExceptionMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
