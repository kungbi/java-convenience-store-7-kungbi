package store.dto;

import java.util.List;

public record AdditionalFreeItemsDto(List<ItemDto> products) {
    public AdditionalFreeItemsDto {
        if (products == null) {
            throw new IllegalArgumentException("products must be provided");
        }
    }
}
