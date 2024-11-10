package store.entity;

import java.util.HashMap;
import java.util.Map;

public class PromotionManagement {
    private final Map<String, Promotion> promotions;

    public PromotionManagement() {
        this.promotions = new HashMap<>();
    }

    public void addPromotion(Promotion promotion) {
        if (promotion == null) {
            throw new IllegalArgumentException();
        }

        promotions.put(promotion.getName(), promotion);
    }

    public Promotion getPromotion(String name) {
        return promotions.get(name);
    }
}
