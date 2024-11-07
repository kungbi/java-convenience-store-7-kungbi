package store.entity;

public interface Product {
    String getName();

    int getPrice();

    int getQuantity();

    void buy(int buyQuantity);

    boolean hasSufficientStock(int checkQuantity);

    boolean isSameName(Product product);
}
