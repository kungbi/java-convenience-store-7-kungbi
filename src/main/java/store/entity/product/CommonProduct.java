package store.entity.product;

public class CommonProduct extends Product {

    public CommonProduct(String name, int price, int quantity) {
        super(name, price, quantity);
    }

    @Override
    public int calculatePrice(int quantity) {
        return getPrice() * quantity;
    }

    @Override
    public ProductType getType() {
        return ProductType.COMMON;
    }

    @Override
    public Product clone() {
        return new CommonProduct(getName(), getPrice(), getQuantity());
    }
}
