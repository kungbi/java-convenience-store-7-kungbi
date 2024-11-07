package store.entity.product;

import store.entity.Promotion;

public class PromotionProduct extends Product {
    private final Promotion promotion;

    public PromotionProduct(String name, int price, int quantity, Promotion promotion) {
        super(name, price, quantity);
        this.promotion = promotion;
    }

    @Override
    public int calculatePrice(int quantity) {
        return 0;
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
