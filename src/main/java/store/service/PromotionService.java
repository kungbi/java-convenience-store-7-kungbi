package store.service;

import java.util.ArrayList;
import java.util.List;
import store.dto.AdditionalFreeItemsDto;
import store.dto.ExcludedPromotionItemsDto;
import store.dto.ItemDto;
import store.dto.PurchaseItemsDto;
import store.entity.ProductStock;
import store.entity.Promotion;
import store.entity.product.ProductType;
import store.entity.product.PromotionProduct;

public class PromotionService {
    private final ProductStock productStock;

    public PromotionService(ProductStock productStock) {
        this.productStock = productStock;
    }

    public AdditionalFreeItemsDto findAdditionalFreeItems(PurchaseItemsDto purchaseItemsDto) {
        List<ItemDto> freePromotionItems = new ArrayList<>();
        for (ItemDto product : purchaseItemsDto.products()) {
            addAdditionalFreeItems(product, freePromotionItems);
        }
        return new AdditionalFreeItemsDto(freePromotionItems);
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

    public int calculateFreeCount(ItemDto purchaseItemDto) {
        Promotion promotion = getValidPromotion(purchaseItemDto.name());
        if (promotion == null) {
            return 0;
        }
        int freeCount = promotion.calculateFreeCount(purchaseItemDto.quantity());
        int promotionStock = productStock.getProductQuantityByUuid(
                ((PromotionProduct) productStock.getProduct(purchaseItemDto.name(), ProductType.PROMOTION)).getUuid());
        return Math.min(freeCount, promotionStock);
    }

    public ItemDto findExcludedPromotionItem(ItemDto product) {
        Promotion promotion = getValidPromotion(product.name());
        if (promotion == null) {
            return new ItemDto(product.name(), 0);
        }

        int excludedCount = promotion.calculateExcludedPromotionCount(
                product.quantity(), productStock.getProductQuantity(product.name(), ProductType.PROMOTION)
        );
        return new ItemDto(product.name(), excludedCount);
    }

    private void addAdditionalFreeItems(ItemDto product, List<ItemDto> freeItems) {
        Promotion promotion = getValidPromotion(product.name());
        if (promotion == null) {
            return;
        }

        int additionalFreeCount = promotion.getAdditionalFreeItemCount(product.quantity());
        int promotionStock = productStock.getProductQuantity(product.name(), ProductType.PROMOTION);
        if (additionalFreeCount > 0 && promotionStock >= product.quantity() + additionalFreeCount) {
            freeItems.add(new ItemDto(product.name(), additionalFreeCount));
        }
    }

    private Promotion getValidPromotion(String productName) {
        if (!productStock.isExistProductWithType(productName, ProductType.PROMOTION)) {
            return null;
        }

        PromotionProduct promoProduct = (PromotionProduct) productStock.getProduct(productName, ProductType.PROMOTION);
        if (!promoProduct.getPromotion().isAvailable()) {
            return null;
        }
        return promoProduct.getPromotion();
    }
}
