package store.validator;

import store.Configuration;
import store.exception.ProductException;
import store.exception.message.ProductExceptionMessage;

public class ProductValidator {

    public static void validate(String name, int price) {
        if (name == null) {
            throw new ProductException(ProductExceptionMessage.NULL_NAME);
        } else if (name.isEmpty()) {
            throw new ProductException(ProductExceptionMessage.EMPTY_NAME);
        } else if (name.length() > Configuration.MAX_PRODUCT_NAME_LENGTH.getInt()) {
            throw new ProductException(ProductExceptionMessage.EXCEED_NAME_LENGTH);
        } else if (price <= 0) {
            throw new ProductException(ProductExceptionMessage.LESS_THAN_OR_EQUAL_ZERO_PRICE);
        }
    }
}
