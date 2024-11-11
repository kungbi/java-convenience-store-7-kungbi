package store.dto;

import java.util.List;
import store.exception.DtoException;
import store.exception.message.DtoExceptionMessage;

public record AdditionalFreeItemsDto(List<ItemDto> products) {
    public AdditionalFreeItemsDto {
        if (products == null) {
            throw new DtoException(DtoExceptionMessage.ITEMS_NULL);
        }
    }
}
