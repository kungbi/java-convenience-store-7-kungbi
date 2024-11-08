package store.service;

import store.dto.PurchaseItemDto;
import store.dto.PurchaseItemsDto;
import store.entity.ProductStock;
import store.entity.product.Product;
import store.entity.product.ProductType;
import store.entity.product.PromotionProduct;

public class ProductStockService {
    private final ProductStock productStock;

    public ProductStockService(ProductStock productStock) {
        this.productStock = productStock;
    }

    public boolean areProductsAvailable(PurchaseItemsDto purchaseItemsDto) {
        for (PurchaseItemDto product : purchaseItemsDto.products()) {
            if (!isProductAvailable(product)) {
                return false;
            }
        }
        return true;
    }

    // private methods

    private boolean isProductAvailable(PurchaseItemDto product) {
        if (!productStock.isExistProduct(product.name())) {
            return false;
        }

        if (isPromotionAvailableAndSufficient(product)) {
            return true;
        }

        return isCommonProductAvailableAndSufficient(product);
    }

    private boolean isPromotionAvailableAndSufficient(PurchaseItemDto product) {
        if (productStock.isExistProductWithType(product.name(), ProductType.PROMOTION)) {
            Product promotionProduct = productStock.getProduct(product.name(), ProductType.PROMOTION);

            if (promotionProduct instanceof PromotionProduct promo && promo.isAvailable()) {
                if (productStock.isSufficientStock(product.name(), ProductType.PROMOTION, product.quantity())) {
                    return true;
                }

                return productStock.isExistProductWithType(product.name(), ProductType.COMMON) &&
                       productStock.isSufficientStock(product.name(), ProductType.COMMON, product.quantity());
            }
        }
        return false;
    }

    private boolean isCommonProductAvailableAndSufficient(PurchaseItemDto product) {
        return productStock.isExistProductWithType(product.name(), ProductType.COMMON) &&
               productStock.isSufficientStock(product.name(), ProductType.COMMON, product.quantity());
    }

}
