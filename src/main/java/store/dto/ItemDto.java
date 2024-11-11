package store.dto;

import store.exception.DtoException;
import store.exception.message.DtoExceptionMessage;

public record ItemDto(String name, int quantity) {
    public ItemDto {
        if (name == null || name.isBlank()) {
            throw new DtoException(DtoExceptionMessage.ITEM_NAME_NULL_OR_BLANK);
        }
        if (quantity < 0) {
            throw new DtoException(DtoExceptionMessage.ITEM_QUANTITY_LESS_THAN_ZERO);
        }
    }
}
