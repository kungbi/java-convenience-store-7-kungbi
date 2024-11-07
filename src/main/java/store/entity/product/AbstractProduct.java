package store.entity.product;

import store.exception.ProductException;
import store.exception.message.ProductExceptionMessage;
import store.validator.ProductValidator;

public class AbstractProduct implements Product {
    private final String name;
    private final int price;
    private int quantity;

    public AbstractProduct(String name, int price, int quantity) {
        ProductValidator.validate(name, price, quantity);
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getPrice() {
        return this.price;
    }

    @Override
    public int getQuantity() {
        return this.quantity;
    }

    @Override
    public void buy(int buyQuantity) {
        if (!hasSufficientStock(buyQuantity)) {
            throw new ProductException(ProductExceptionMessage.INSUFFICIENT_STOCK);
        }
        this.quantity -= buyQuantity;
    }

    @Override
    public boolean hasSufficientStock(int checkQuantity) {
        return this.quantity >= checkQuantity;
    }

    @Override
    public boolean isSameName(Product product) {
        return this.name.equalsIgnoreCase(product.getName());
    }
}
