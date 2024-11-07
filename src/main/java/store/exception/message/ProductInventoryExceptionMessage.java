package store.exception.message;

public enum ProductInventoryExceptionMessage implements ExceptionMessage {
    NULL_PRODUCT("상품이 NULL 입니다."),
    DUPLICATE_PRODUCT("이미 존재하는 상품입니다."),
    NOT_EXIST_PRODUCT("존재하지 않는 상품입니다.");

    private final String message;

    ProductInventoryExceptionMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
