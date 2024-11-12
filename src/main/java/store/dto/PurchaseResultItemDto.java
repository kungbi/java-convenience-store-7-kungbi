package store.dto;

import store.exception.DtoException;
import store.exception.message.DtoExceptionMessage;

public record PurchaseResultItemDto(String name, int quantity, int price) {
    public PurchaseResultItemDto {
        if (name == null || name.isBlank()) {
            throw new DtoException(DtoExceptionMessage.ITEM_NAME_NULL_OR_BLANK);
        }
        if (quantity < 0) {
            throw new DtoException(DtoExceptionMessage.ITEM_QUANTITY_LESS_THAN_ZERO);
        }
        if (price < 0) {
            throw new DtoException(DtoExceptionMessage.ITEM_PRICE_LESS_THAN_ZERO);
        }
    }
}
