package store.dto;

import java.util.List;

public record ExcludedPromotionItemsDto(List<ItemDto> items) {
    public ExcludedPromotionItemsDto {
        if (items == null) {
            throw new IllegalArgumentException("items must be provided");
        }
    }
}
