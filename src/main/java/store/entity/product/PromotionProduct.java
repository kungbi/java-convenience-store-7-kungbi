package store.entity.product;

public class PromotionProduct implements Product {


    @Override
    public String getName() {
        return "";
    }

    @Override
    public int getPrice() {
        return 0;
    }

    @Override
    public int getQuantity() {
        return 0;
    }

    @Override
    public void buy(int buyQuantity) {

    }

    @Override
    public boolean hasSufficientStock(int checkQuantity) {
        return false;
    }

    @Override
    public boolean isSameName(Product product) {
        return false;
    }
}
