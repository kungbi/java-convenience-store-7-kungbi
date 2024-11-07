package store.entity.product;

import store.entity.Promotion;
import store.exception.ProductException;
import store.exception.message.ProductExceptionMessage;

public class PromotionProduct extends Product {
    private final Promotion promotion;

    public PromotionProduct(String name, int price, int quantity, Promotion promotion) {
        super(name, price, quantity);
        this.promotion = promotion;
    }

    @Override
    public int calculatePrice(int quantity) {
        if (quantity < 0) {
            throw new ProductException(ProductExceptionMessage.NEGATIVE_QUANTITY);
        }
        if (!promotion.isAvailable()) {
            return getPrice() * quantity;
        }

        int freeQuantity = promotion.calculateFreeQuantity(quantity);
        return getPrice() * (quantity - freeQuantity);
    }

    @Override
    public ProductType getType() {
        return ProductType.PROMOTION;
    }

    @Override
    public Product clone() {
        return new PromotionProduct(getName(), getPrice(), getQuantity(), promotion);
    }
}
