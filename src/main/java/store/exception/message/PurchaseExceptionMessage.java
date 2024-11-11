package store.exception.message;

public enum PurchaseExceptionMessage implements ExceptionMessage {
    PRODUCT_STOCK_NOT_ENOUGH("재고가 부족합니다."),
    PRODUCT_NOT_FOUND("상품을 찾을 수 없습니다."),
    PRODUCT_STOCK_NOT_FOUND("상품 재고를 찾을 수 없습니다."),

    PURCHASE_EXCEPTION_MESSAGE("구매할 상품이 없습니다. 다시 입력해 주세요.");

    private final String message;

    PurchaseExceptionMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
