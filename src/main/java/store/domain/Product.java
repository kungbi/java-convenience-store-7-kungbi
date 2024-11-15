package store.domain;

import store.exception.ProductCreationException;

public class Product {
    private final String name;
    private final int price;

    public Product(String name, int price) {
        if (name == null || name.isBlank()) {
            throw new ProductCreationException();
        }
        if (price < 0) {
            throw new ProductCreationException();
        }

        this.name = name;
        this.price = price;
    }


}
