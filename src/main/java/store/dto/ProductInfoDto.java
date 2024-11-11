package store.dto;

import store.exception.DtoException;
import store.exception.message.DtoExceptionMessage;

public record ProductInfoDto(String name, int price, int quantity, String promotion) {
    public ProductInfoDto {
        if (name == null || name.isBlank()) {
            throw new DtoException(DtoExceptionMessage.ITEM_NAME_NULL_OR_BLANK);
        }
        if (price < 0) {
            throw new DtoException(DtoExceptionMessage.ITEM_PRICE_LESS_THAN_ZERO);
        }
        if (quantity < 0) {
            throw new DtoException(DtoExceptionMessage.ITEM_QUANTITY_LESS_THAN_ZERO);
        }
        if (promotion == null || promotion.isBlank()) {
            throw new DtoException(DtoExceptionMessage.ITEM_PROMOTION_NULL_OR_BLANK);
        }
    }

}
