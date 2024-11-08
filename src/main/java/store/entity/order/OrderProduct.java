package store.entity.order;

import store.exception.OrderException;

public class OrderProduct {
    private final String productName;
    private final int quantity;
    private final int unitPrice;
    private final int promotionDiscount;
    private final int membershipDiscount;

    public OrderProduct(String productName, int quantity, int unitPrice, int promotionDiscount,
                        int membershipDiscount) {
        if (quantity < 1) {
            throw new OrderException("주문 수량은 1개 이상이어야 합니다.");
        }
        if (unitPrice < 0 || promotionDiscount < 0 || membershipDiscount < 0) {
            throw new OrderException("할인 금액은 음수가 될 수 없습니다.");
        }
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.promotionDiscount = promotionDiscount;
        this.membershipDiscount = membershipDiscount;
    }

    public int calculateTotalPrice() {
        int totalPrice = getOriginalPrice() - promotionDiscount - membershipDiscount;
        return Math.max(totalPrice, 0);
    }

    public int getOriginalPrice() {
        return quantity * unitPrice;
    }

    public int getPromotionDiscount() {
        return promotionDiscount;
    }

    public int getMembershipDiscount() {
        return membershipDiscount;
    }
}
