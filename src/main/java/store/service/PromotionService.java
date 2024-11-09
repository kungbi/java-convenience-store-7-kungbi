package store.service;

import java.util.ArrayList;
import store.dto.PurchaseItemDto;
import store.dto.PurchaseItemsDto;
import store.entity.ProductStock;
import store.entity.product.ProductType;
import store.entity.product.PromotionProduct;

public class PromotionService {
    private final ProductStock productStock;

    public PromotionService(ProductStock productStock) {
        this.productStock = productStock;
    }

    public PurchaseItemsDto findAdditionalFreeItems(PurchaseItemsDto purchaseItemsDto) {
        ArrayList<PurchaseItemDto> freePromotionItems = new ArrayList<>();
        for (PurchaseItemDto product : purchaseItemsDto.products()) {
            // 프로모션 상품이 있을 경우,
            if (productStock.isExistProductWithType(product.name(), ProductType.PROMOTION)) {
                // 프로모션 상품이 존재하고,
                // 그 상품이 PromotionProduct 타입이라면,
                if (productStock.getProduct(product.name(),
                        ProductType.PROMOTION) instanceof PromotionProduct promotionProduct
                    && promotionProduct.isAvailable()) {
                    // 그 상품이 사용 가능한 상태라면,

                    int additionalFreeItemCount = promotionProduct.getPromotion()
                            .getAdditionalFreeItemCount(product.quantity());

                    int productQuantity = productStock.getProductQuantity(product.name(), ProductType.PROMOTION);
                    if (product.quantity() + additionalFreeItemCount <= productQuantity) {
                        freePromotionItems.add(new PurchaseItemDto(product.name(), additionalFreeItemCount));
                    }
                }
            }
        }

        return new PurchaseItemsDto(freePromotionItems);
    }

    public int calculateFreeCount(PurchaseItemDto purchaseItemDto) {
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
}
