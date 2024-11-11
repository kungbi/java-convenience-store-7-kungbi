package store;

public enum Configuration {
    MAX_PRODUCT_NAME_LENGTH(50),
    MAX_PROMOTION_NAME_LENGTH(50),

    PROMOTION_BUY_QUANTITY_MIN(1),
    PROMOTION_FREE_QUANTITY_MIN(1),

    MEMBERSHIP_DISCOUNT_RATE(30),
    MEMBERSHIP_DISCOUNT_MAX_AMOUNT(8000),

    PRODUCT_DATA_FILE("products.md"),
    PROMOTION_DATA_FILE("promotions.md");

    private final Object value;

    Configuration(Object value) {
        this.value = value;
    }

    public int getInt() {
        return (int) value;
    }

    public String getString() {
        return (String) value;
    }
}
