package store.entity.order;

public class OrderPrice {
    private final int originalPrice;
    private final int promotionDiscount;
    private final int membershipDiscount;


    public OrderPrice(int originalPrice, int promotionDiscount, int membershipDiscount) {
        if (originalPrice < 0 || promotionDiscount < 0 || membershipDiscount < 0) {
            throw new IllegalArgumentException();
        }
        this.originalPrice = originalPrice;
        this.promotionDiscount = promotionDiscount;
        this.membershipDiscount = membershipDiscount;
    }

    public int calculateTotalPrice() {
        int totalPrice = originalPrice - promotionDiscount - membershipDiscount;
        return Math.max(totalPrice, 0);
    }

    public int getOriginalPrice() {
        return originalPrice;
    }

    public int getPromotionDiscount() {
        return promotionDiscount;
    }

    public int getMembershipDiscount() {
        return membershipDiscount;
    }
}
