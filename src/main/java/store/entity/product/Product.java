package store.entity.product;

import store.exception.ProductException;
import store.exception.message.ProductExceptionMessage;
import store.validator.ProductValidator;

public abstract class Product {
    private final String name;
    private final int price;
    private int quantity;

    public Product(String name, int price, int quantity) {
        ProductValidator.validate(name, price, quantity);
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getName() {
        return this.name;
    }

    public int getPrice() {
        return this.price;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public boolean isSameName(Product product) {
        return this.name.equalsIgnoreCase(product.getName());
    }

    public void buy(int quantity) {
        if (!hasSufficientStock(quantity)) {
            throw new ProductException(ProductExceptionMessage.INSUFFICIENT_STOCK);
        }
        this.quantity -= quantity;
    }

    public boolean hasSufficientStock(int checkQuantity) {
        return this.quantity >= checkQuantity;
    }

    public abstract int calculatePrice(int quantity);

    public abstract ProductType getType();

    public abstract Product clone();
}
