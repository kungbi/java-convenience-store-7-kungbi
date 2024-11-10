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
            extracted(product, freePromotionItems);
        }
        return new AdditionalFreeItemsDto(freePromotionItems);
    }

    public ExcludedPromotionItemsDto findExcludedPromotionItems(PurchaseItemsDto purchaseItemsDto) {
        List<ItemDto> excludedPromotionItems = new ArrayList<>();
        for (ItemDto product : purchaseItemsDto.products()) {
            if (!productStock.isExistProductWithType(product.name(), ProductType.PROMOTION)) {
                continue;
            }

            Promotion promotion = ((PromotionProduct) productStock.getProduct(product.name(),
                    ProductType.PROMOTION)).getPromotion();
            if (!promotion.isAvailable()) {
                continue;
            }

            if (!productStock.isExistProductWithType(product.name(), ProductType.PROMOTION)) {
                continue;
            }

            int excludedPromotionCount = promotion.calculateExcludedPromotionCount(product.quantity(),
                    productStock.getProductQuantity(product.name(), ProductType.PROMOTION));
            excludedPromotionItems.add(new ItemDto(product.name(), excludedPromotionCount));
        }
        return new ExcludedPromotionItemsDto(excludedPromotionItems);
    }


    public int calculateFreeCount(ItemDto purchaseItemDto) {
        if (!productStock.isExistProductWithType(purchaseItemDto.name(), ProductType.PROMOTION)) {
            return 0;
        }
        PromotionProduct promotionProduct = (PromotionProduct) productStock.getProduct(purchaseItemDto.name(),
                ProductType.PROMOTION);

        if (!promotionProduct.isAvailable()) {
            return 0;
        }

        return promotionProduct.getPromotion().calculateFreeCount(purchaseItemDto.quantity());
    }

    private void extracted(ItemDto product, List<ItemDto> freePromotionItems) {
        if (!productStock.isExistProductWithType(product.name(), ProductType.PROMOTION)) {
            return;
        }

        Promotion promotion = ((PromotionProduct) productStock.getProduct(product.name(),
                ProductType.PROMOTION)).getPromotion();

        if (!promotion.isAvailable()) {
            return;
        }

        int additionalFreeItemCount = promotion.getAdditionalFreeItemCount(product.quantity());
        int productQuantity = productStock.getProductQuantity(product.name(), ProductType.PROMOTION);
        if (product.quantity() + additionalFreeItemCount <= productQuantity) {
            freePromotionItems.add(new ItemDto(product.name(), additionalFreeItemCount));
        }
    }
}
