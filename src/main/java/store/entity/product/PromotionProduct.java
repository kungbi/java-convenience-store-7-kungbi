package store.entity.product;

import store.entity.Promotion;

public class PromotionProduct extends AbstractProduct {
    private final Promotion promotion;

    public PromotionProduct(String name, int price, int quantity, Promotion promotion) {
        super(name, price, quantity);
        this.promotion = promotion;
    }

}
