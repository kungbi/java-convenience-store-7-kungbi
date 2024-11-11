package store.exception.message;

public enum DtoExceptionMessage implements ExceptionMessage {
    ITEMS_NULL("상품에 대한 정보가 NULL 입니다."),
    ITEMS_NULL_OR_EMPTY("상품에 대한 정보가 NULL 또는 빈 리스트입니다."),
    FREE_ITEMS_NULL("증정 상품에 대한 정보가 NULL 입니다."),
    ITEM_NAME_NULL_OR_BLANK("상품 이름이 NULL 또는 빈 문자열입니다."),
    ITEM_QUANTITY_LESS_THAN_ZERO("상품 수량이 0보다 작습니다."),
    ITEM_PRICE_LESS_THAN_ZERO("상품 가격이 0보다 작습니다."),
    ITEM_PROMOTION_NULL_OR_BLANK("상품 프로모션이 NULL 또는 빈 문자열입니다."),
    TOTAL_PRICE_LESS_THAN_ZERO("총 가격이 0보다 작습니다."),
    PROMOTION_DISCOUNT_LESS_THAN_ZERO("프로모션 할인액이 0보다 작습니다."),
    MEMBERSHIP_DISCOUNT_LESS_THAN_ZERO("멤버십 할인액이 0보다 작습니다."),
    PAYMENT_AMOUNT_LESS_THAN_ZERO("결제 금액이 0보다 작습니다."),
    START_DATE_NULL("시작 날짜가 NULL 입니다."),
    END_DATE_NULL("종료 날짜가 NULL 입니다."),
    ;

    private final String message;

    DtoExceptionMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
