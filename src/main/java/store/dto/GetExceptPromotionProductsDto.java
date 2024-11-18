package store.dto;

import java.util.List;

public class GetExceptPromotionProductsDto {
    public record GetExceptPromotionProductsInputDto(List<ItemDto> purchaseItems) {
    }

    public record GetExceptPromotionProductsOutputDto(List<ItemDto> exceptedItems) {
    }
}
