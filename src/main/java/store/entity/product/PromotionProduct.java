package store.entity.product;

import store.entity.Promotion;

public class PromotionProduct extends Product {
    private final Promotion promotion;

    public PromotionProduct(String name, int price, Promotion promotion) {
        super(name, price);
        this.promotion = promotion;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public boolean isAvailable() {
        return promotion.isAvailable();
    }

    @Override
    public ProductType getType() {
        return ProductType.PROMOTION;
    }

    @Override
    public Product clone() {
        return new PromotionProduct(getName(), getPrice(), promotion);
    }
}
