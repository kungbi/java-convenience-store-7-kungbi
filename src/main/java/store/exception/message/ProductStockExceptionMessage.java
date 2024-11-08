package store.exception.message;

public enum ProductStockExceptionMessage implements ExceptionMessage {
    NULL_PRODUCT("상품 객체가 NULL 입니다."),
    NULL_NAME("상품의 이름이 NULL 입니다."),
    NULL_TYPE("상품 타입이 NULL 입니다."),
    DUPLICATE_PRODUCT("이미 존재하는 상품입니다."),
    NOT_EXIST_PRODUCT("존재하지 않는 상품입니다."),
    INVALID_QUANTITY("상품의 수량이 올바르지 않습니다."),
    NEGATIVE_QUANTITY("상품 수량이 0개 미만입니다."),
    INSUFFICIENT_STOCK("상품 재고가 부족합니다.");

    private final String message;

    ProductStockExceptionMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
