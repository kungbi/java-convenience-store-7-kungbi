package store.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Stock {
    private final Map<Integer, Integer> stock = new HashMap<>();

    public void add(int id, int quantity) {
        if (this.exists(id)) {
            throw new IllegalArgumentException();
        }
        if (quantity < 0) {
            throw new IllegalArgumentException();
        }

        stock.put(id, quantity);
    }

    public int getQuantity(int id) {
        if (!exists(id)) {
            throw new IllegalArgumentException();
        }
        return stock.get(id);
    }

    public void decrease(int id, int quantity) {
        if (!exists(id)) {
            throw new IllegalArgumentException();
        }
        if (stock.get(id) - quantity < 0) {
            throw new IllegalArgumentException();
        }
        stock.put(id, stock.get(id) - quantity);
    }

    public boolean exists(int id) {
        return this.findById(id).isPresent();
    }

    private Optional<Integer> findById(int id) {
        Integer quantity = stock.get(id);
        return Optional.ofNullable(quantity);
    }
}
