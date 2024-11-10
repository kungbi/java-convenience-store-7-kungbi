package store.dto;

import java.util.List;

public record PurchaseRequestDto(List<ItemDto> products, boolean isMembership) {
    public PurchaseRequestDto {
        if (products == null) {
            throw new IllegalArgumentException("products must be provided");
        }
    }
}
