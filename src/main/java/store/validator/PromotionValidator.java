package store.validator;

import store.entity.ProductStock;
import store.entity.Promotion;
import store.entity.product.ProductType;
import store.entity.product.PromotionProduct;

public class PromotionValidator {
    private final ProductStock productStock;

    public PromotionValidator(ProductStock productStock) {
        this.productStock = productStock;
    }


    public Promotion getValidPromotion(String productName) {
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
