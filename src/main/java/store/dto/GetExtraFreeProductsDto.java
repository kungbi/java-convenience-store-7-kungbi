package store.dto;

import java.util.List;

public class GetExtraFreeProductsDto {
    public record GetExtraFreeProductsInputDto(List<ItemDto> purchaseItems) {

    }

    public record GetExtraFreeProductsOutputDto(List<ItemDto> additionalProducts) {

    }
}
