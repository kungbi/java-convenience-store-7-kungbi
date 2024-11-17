package store.domain;

import store.config.ProductType;
import store.exception.ProductCreationException;

public class Product {
    private final int id;
    private final String name;
    private final int price;
    private final ProductType type;

    public Product(int id, String name, int price, ProductType type) {
        if (name == null || name.isBlank()) {
            throw new ProductCreationException();
        }
        if (price < 0) {
            throw new ProductCreationException();
        }
        if (type == null) {
            throw new ProductCreationException();
        }

        this.id = id;
        this.type = type;
        this.name = name;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public ProductType getType() {
        return type;
    }
}
