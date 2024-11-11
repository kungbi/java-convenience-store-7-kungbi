package store.service.PromotionService;

import java.util.ArrayList;
import java.util.List;
import store.dto.ExcludedPromotionItemsDto;
import store.dto.ItemDto;
import store.dto.PurchaseItemsDto;
import store.entity.ProductStock;
import store.entity.Promotion;
import store.entity.product.ProductType;
import store.validator.PromotionValidator;

public class ExcludedPromotionItemsCalculator {
    private final ProductStock productStock;
    private final PromotionValidator promotionValidator;

    public ExcludedPromotionItemsCalculator(ProductStock productStock, PromotionValidator promotionValidator) {
        this.productStock = productStock;
        this.promotionValidator = promotionValidator;
    }

    public ExcludedPromotionItemsDto findExcludedPromotionItems(PurchaseItemsDto purchaseItemsDto) {
        List<ItemDto> excludedPromotionItems = new ArrayList<>();
        for (ItemDto product : purchaseItemsDto.products()) {
            ItemDto excludedItem = findExcludedPromotionItem(product);
            if (excludedItem.quantity() > 0) {
                excludedPromotionItems.add(excludedItem);
            }
        }
        return new ExcludedPromotionItemsDto(excludedPromotionItems);
    }

    public ItemDto findExcludedPromotionItem(ItemDto product) {
        Promotion promotion = promotionValidator.getValidPromotion(product.name());
        if (promotion == null) {
            return new ItemDto(product.name(), 0);
        }

        int excludedCount = promotion.calculateExcludedPromotionCount(product.quantity(),
                productStock.getProductQuantity(product.name(), ProductType.PROMOTION));
        return new ItemDto(product.name(), excludedCount);
    }
}
