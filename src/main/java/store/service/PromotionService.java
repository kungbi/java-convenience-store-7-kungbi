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
            findAdditionalFreeItems(product, freePromotionItems);
        }
        return new AdditionalFreeItemsDto(freePromotionItems);
    }

    public ExcludedPromotionItemsDto findExcludedPromotionItems(PurchaseItemsDto purchaseItemsDto) {
        List<ItemDto> excludedPromotionItems = new ArrayList<>();
        for (ItemDto product : purchaseItemsDto.products()) {
            ItemDto excludedPromotionItem = findExcludedPromotionItem(product);
            if (excludedPromotionItem.quantity() > 0) {
                excludedPromotionItems.add(excludedPromotionItem);
            }
        }
        return new ExcludedPromotionItemsDto(excludedPromotionItems);
    }

    public ItemDto findExcludedPromotionItem(ItemDto product) {
        // 프로모션 상품이 존재하지 않거나, 프로모션이 적용 불가능한 경우 0을 반환
        if (!productStock.isExistProductWithType(product.name(), ProductType.PROMOTION)) {
            return new ItemDto(product.name(), 0);
        }

        PromotionProduct promotionProduct = (PromotionProduct) productStock.getProduct(product.name(),
                ProductType.PROMOTION);
        Promotion promotion = promotionProduct.getPromotion();

        // 프로모션이 적용 불가능한 경우 0을 반환
        if (!promotion.isAvailable()) {
            return new ItemDto(product.name(), 0);
        }

        // 프로모션 제외 수량 계산
        int excludedPromotionCount = promotion.calculateExcludedPromotionCount(
                product.quantity(),
                productStock.getProductQuantity(product.name(), ProductType.PROMOTION)
        );

        // 항상 ItemDto 반환, 제외 수량이 없으면 0을 설정
        return new ItemDto(product.name(), excludedPromotionCount);
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

    private void findAdditionalFreeItems(ItemDto product, List<ItemDto> freePromotionItems) {
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
