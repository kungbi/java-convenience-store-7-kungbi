package store.exception.message;

public enum ProductExceptionMessage implements ExceptionMessage {
    NULL_NAME("상품 이름이 NULL 입니다."),
    EMPTY_NAME("상품 이름이 빈 문자열입니다."),
    EXCEED_NAME_LENGTH("상품 이름이 50자를 초과했습니다."),
    LESS_THAN_OR_EQUAL_ZERO_PRICE("상품 가격이 0원 이하입니다."),
    NEGATIVE_QUANTITY("상품 수량이 0개 미만입니다."),
    INSUFFICIENT_STOCK("상품 재고가 부족합니다.");

    private final String message;

    ProductExceptionMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
