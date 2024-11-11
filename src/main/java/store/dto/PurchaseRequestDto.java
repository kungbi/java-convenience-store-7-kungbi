package store.dto;

import java.util.List;
import store.exception.DtoException;
import store.exception.message.DtoExceptionMessage;

public record PurchaseRequestDto(List<ItemDto> products, boolean isMembership) {
    public PurchaseRequestDto {
        if (products == null || products.isEmpty()) {
            throw new DtoException(DtoExceptionMessage.ITEMS_NULL_OR_EMPTY);
        }
    }
}
