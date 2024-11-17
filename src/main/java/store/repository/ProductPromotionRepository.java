package store.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import store.domain.Promotion;

public class ProductPromotionRepository {
    Map<String, Promotion> promotions = new HashMap<>();

    public void add(String productName, Promotion promotion) {
        if (productName == null || promotion == null) {
            throw new IllegalArgumentException();
        }
        if (this.exists(productName)) {
            throw new IllegalArgumentException();
        }

        promotions.put(productName, promotion);
    }

    public Optional<Promotion> findByProductName(String productName) {
        if (productName == null) {
            throw new IllegalArgumentException();
        }

        if (!this.exists(productName)) {
            return Optional.empty();
        }
        return Optional.of(promotions.get(productName));
    }

    public boolean exists(String productName) {
        return promotions.containsKey(productName);
    }
}
