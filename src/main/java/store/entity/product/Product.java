package store.entity.product;

import java.util.UUID;
import store.validator.ProductValidator;

public abstract class Product {
    private final String uuid;
    private final String name;
    private final int price;

    public Product(String name, int price) {
        ProductValidator.validate(name, price);
        this.uuid = UUID.randomUUID().toString();
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return this.name;
    }

    public String getUuid() {
        return this.uuid;
    }

    public int getPrice() {
        return this.price;
    }

    public abstract ProductType getType();

}
