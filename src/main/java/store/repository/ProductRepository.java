package store.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import store.config.ProductType;
import store.domain.Product;
import store.exception.IllegalArgumentMessage;
import store.exception.ProductRepositoryException;

public class ProductRepository implements Repository<Product> {
    private final Map<String, Map<ProductType, Product>> products = new HashMap<>();
    private int idCounter;

    public Optional<List<Product>> findByName(String name) {
        if (name == null) {
            throw new ProductRepositoryException();
        }

        if (!products.containsKey(name)) {
            return Optional.empty();
        }
        return Optional.of(new ArrayList<>(products.get(name).values()));
    }

    public Optional<Product> findByNameAndType(String name, ProductType type) {
        if (name == null) {
            throw new ProductRepositoryException();
        }
        if (type == null) {
            throw new ProductRepositoryException();
        }

        if (!products.containsKey(name)) {
            return Optional.empty();
        }
        if (!products.get(name).containsKey(type)) {
            return Optional.empty();
        }
        return Optional.of(products.get(name).get(type));
    }

    public void add(String name, int price, ProductType type) {
        if (existsWithType(name, type)) {
            throw new ProductRepositoryException(IllegalArgumentMessage.PRODUCT_DUPLICATE);
        }

        products.putIfAbsent(name, new HashMap<>());
        products.get(name).put(type, new Product(idCounter++, name, price, type));
    }

    @Override
    public void remove(Product entity) {
        // 구현 내용 없음
    }

    @Override
    public int getSize() {
        int size = 0;
        for (String key : products.keySet()) {
            size += products.get(key).size();
        }
        return size;
    }

    @Override
    public Optional<List<Product>> findAll() {
        List<Product> returnProducts = new ArrayList<>();
        for (String key : products.keySet()) {
            returnProducts.addAll(products.get(key).values());
        }
        if (returnProducts.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(returnProducts);
    }

    @Override
    public void update(String name, Product newData) {
        // 구현 내용 없음
    }

    @Override
    public boolean exists(String name) {
        return findByName(name).isPresent();
    }

    public boolean existsWithType(String name, ProductType type) {
        return findByNameAndType(name, type).isPresent();
    }

    @Override
    public void clear() {

    }

}
