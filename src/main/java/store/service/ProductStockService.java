package store.service;

import store.dto.PurchaseItemDto;
import store.dto.PurchaseItemsDto;
import store.entity.ProductStock;
import store.entity.product.Product;
import store.entity.product.ProductType;
import store.entity.product.PromotionProduct;
import store.exception.ProductException;
import store.exception.ProductStockException;
import store.exception.message.ProductStockExceptionMessage;

public class ProductStockService {
    private final ProductStock productStock;

    public ProductStockService(ProductStock productStock) {
        this.productStock = productStock;
    }

    public boolean validateProductsAvailability(PurchaseItemsDto purchaseItemsDto) {
        for (PurchaseItemDto product : purchaseItemsDto.products()) {
            if (!productStock.isExistProduct(product.name())) {
                throw new ProductStockException(ProductStockExceptionMessage.NOT_EXIST_PRODUCT);
            }

            validatePromotionProductStock(product);
            validateCommonProductStock(product);
        }
        return true;
    }

    // private methods

    private void validatePromotionProductStock(PurchaseItemDto product) {
        if (productStock.isExistProductWithType(product.name(), ProductType.PROMOTION)) {
            Product promotionProduct = productStock.getProduct(product.name(), ProductType.PROMOTION);

            if (promotionProduct instanceof PromotionProduct promo && promo.isAvailable()) {

                // 프로모션이 적용 가능하고, 프로모션 상품의 재고가 부족하면서, 일반 상품이 없는 경우
                // 프로모션이 적용 가능하고, 프로모션 상품의 재고가 부족하면서, 일반 상품의 재고가 부족한 경우
                if (!productStock.isSufficientStock(product.name(), ProductType.PROMOTION, product.quantity())) {
                    if (!productStock.isExistProductWithType(product.name(), ProductType.COMMON)) {
                        throw new ProductStockException(ProductStockExceptionMessage.INSUFFICIENT_STOCK);
                    }

                    if (!productStock.isSufficientStock(product.name(), ProductType.COMMON, product.quantity())) {
                        throw new ProductException(ProductStockExceptionMessage.INSUFFICIENT_STOCK);
                    }
                }

            }
        }
    }

    private void validateCommonProductStock(PurchaseItemDto product) {
        if (productStock.isExistProductWithType(product.name(), ProductType.COMMON) &&
            !productStock.isSufficientStock(product.name(), ProductType.COMMON, product.quantity())) {
            throw new ProductStockException(ProductStockExceptionMessage.INSUFFICIENT_STOCK);
        }
    }

}
