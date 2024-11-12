package store.dto;

import java.util.List;
import store.exception.DtoException;
import store.exception.message.DtoExceptionMessage;

public record ExcludedPromotionItemsDto(List<ItemDto> items) {
    public ExcludedPromotionItemsDto {
        if (items == null) {
            throw new DtoException(DtoExceptionMessage.ITEMS_NULL);
        }
    }
}
