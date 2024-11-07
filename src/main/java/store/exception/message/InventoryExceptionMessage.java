package store.exception.message;

public enum InventoryExceptionMessage implements ExceptionMessage {
    NULL_PRODUCT("상품 객체가 NULL 입니다."),
    NULL_NAME("상품의 이름이 NULL 입니다."),
    NULL_TYPE("상품 타입이 NULL 입니다."),
    DUPLICATE_PRODUCT("이미 존재하는 상품입니다."),
    NOT_EXIST_PRODUCT("존재하지 않는 상품입니다.");

    private final String message;

    InventoryExceptionMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
