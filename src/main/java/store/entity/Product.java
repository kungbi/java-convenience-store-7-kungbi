package store.entity;

import store.exception.ProductException;
import store.exception.message.ProductExceptionMessage;

public class Product {
    private final String name;
    private final int price;
    private int quantity;
//    private final Promotion promotion;

    public Product(String name, int price, int quantity, String promotion) {
        if (name == null) {
            throw new ProductException(ProductExceptionMessage.NULL_NAME);
        } else if (name.isEmpty()) {
            throw new ProductException(ProductExceptionMessage.EMPTY_NAME);
        } else if (name.length() > 50) {
            throw new ProductException(ProductExceptionMessage.EXCEED_NAME_LENGTH);
        } else if (price <= 0) {
            throw new ProductException(ProductExceptionMessage.LESS_THAN_OR_EQUAL_ZERO_PRICE);
        } else if (quantity < 0) {
            throw new ProductException(ProductExceptionMessage.NEGATIVE_QUANTITY);
        }

        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void buy(int buyQuantity) {
        if (!hasSufficientStock(buyQuantity)) {
            throw new ProductException(ProductExceptionMessage.INSUFFICIENT_STOCK);
        }
        this.quantity -= buyQuantity;
    }

    public boolean hasSufficientStock(int checkQuantity) {
        return this.quantity >= checkQuantity;
    }

    public boolean isSameName(Product product) {
        return this.name.equalsIgnoreCase(product.getName());
    }

}
