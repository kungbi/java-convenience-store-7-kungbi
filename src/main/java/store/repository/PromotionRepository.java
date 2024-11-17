package store.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import store.domain.Promotion;

public class PromotionRepository implements Repository<Promotion> {
    Map<String, Promotion> promotions = new HashMap<>();

    public void add(Promotion entity) {
        if (this.exists(entity.getName())) {
            throw new IllegalArgumentException("Promotion already exists");
        }
        this.promotions.put(entity.getName(), entity);
    }

    @Override
    public void remove(Promotion entity) {

    }

    @Override
    public int getSize() {
        return promotions.size();
    }

    @Override
    public Optional<List<Promotion>> findAll() {
        return Optional.empty();
    }

    @Override
    public void update(String name, Promotion newData) {

    }

    @Override
    public boolean exists(String name) {
        return this.promotions.containsKey(name);
    }

    @Override
    public void clear() {

    }
}
