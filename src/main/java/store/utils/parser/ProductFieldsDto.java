package store.utils.parser;

import store.exception.DtoException;
import store.exception.message.DtoExceptionMessage;

public record ProductFieldsDto(String name, int price, int quantity, String promotionName) {
    public ProductFieldsDto {
        if (name == null || name.isBlank()) {
            throw new DtoException(DtoExceptionMessage.ITEM_NAME_NULL_OR_BLANK);
        }
        if (price <= 0) {
            throw new DtoException(DtoExceptionMessage.ITEM_PRICE_LESS_THAN_ZERO);
        }
        if (quantity <= 0) {
            throw new DtoException(DtoExceptionMessage.ITEM_QUANTITY_LESS_THAN_ZERO);
        }
    }
}
