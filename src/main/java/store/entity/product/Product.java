package store.entity.product;

import store.validator.ProductValidator;

public abstract class Product {
    private final String name;
    private final int price;

    public Product(String name, int price) {
        ProductValidator.validate(name, price);
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return this.name;
    }

    public int getPrice() {
        return this.price;
    }

    public boolean isSameName(Product product) {
        return this.name.equalsIgnoreCase(product.getName());
    }

    public abstract ProductType getType();

    public abstract Product clone();
}
