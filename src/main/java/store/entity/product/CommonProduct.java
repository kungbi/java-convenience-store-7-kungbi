package store.entity.product;

public class CommonProduct extends Product {

    public CommonProduct(String name, int price) {
        super(name, price);
    }

    @Override
    public ProductType getType() {
        return ProductType.COMMON;
    }

}
