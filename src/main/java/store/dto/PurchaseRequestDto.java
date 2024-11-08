package store.dto;

import java.util.List;

public record PurchaseRequestDto(List<PurchaseItemDto> products) {
}
