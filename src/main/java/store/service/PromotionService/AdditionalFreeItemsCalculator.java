package store.service.PromotionService;

import java.util.ArrayList;
import java.util.List;
import store.dto.AdditionalFreeItemsDto;
import store.dto.ItemDto;
import store.dto.PurchaseItemsDto;
import store.entity.ProductStock;
import store.entity.Promotion;
import store.entity.product.ProductType;
import store.validator.PromotionValidator;

public class AdditionalFreeItemsCalculator {
    private final PromotionValidator promotionValidator;
    private final ProductStock productStock;

    public AdditionalFreeItemsCalculator(ProductStock productStock, PromotionValidator promotionValidator) {
        this.promotionValidator = promotionValidator;
        this.productStock = productStock;
    }

    public AdditionalFreeItemsDto findAdditionalFreeItems(PurchaseItemsDto purchaseItemsDto) {
        List<ItemDto> freePromotionItems = new ArrayList<>();
        for (ItemDto product : purchaseItemsDto.products()) {
            addAdditionalFreeItems(product, freePromotionItems);
        }
        return new AdditionalFreeItemsDto(freePromotionItems);
    }

    public void addAdditionalFreeItems(ItemDto product, List<ItemDto> freeItems) {
        Promotion promotion = promotionValidator.getValidPromotion(product.name());
        if (promotion == null) {
            return;
        }

        int additionalFreeCount = promotion.getAdditionalFreeItemCount(product.quantity());
        int promotionStock = productStock.getProductQuantity(product.name(), ProductType.PROMOTION);
        if (additionalFreeCount > 0 && promotionStock >= product.quantity() + additionalFreeCount) {
            freeItems.add(new ItemDto(product.name(), additionalFreeCount));
        }
    }
}
