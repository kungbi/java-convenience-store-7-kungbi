package store.dto;


import java.util.List;

public record PurchaseResultDto(
        List<PurchaseResultItemDto> purchaseItems,
        List<ItemDto> freeItems,
        int totalAmount,
        int promotionDiscountAmount,
        int membershipDiscountAmount,
        int paymentAmount
) {
    public PurchaseResultDto {
        if (purchaseItems == null) {
            throw new IllegalArgumentException("purchaseItems must be provided");
        }
        if (freeItems == null) {
            throw new IllegalArgumentException("freeItems must be provided");
        }
        if (totalAmount < 0) {
            throw new IllegalArgumentException("totalAmount must be greater than or equal to 0");
        }
        if (promotionDiscountAmount < 0) {
            throw new IllegalArgumentException("promotionDiscountAmount must be greater than or equal to 0");
        }
        if (membershipDiscountAmount < 0) {
            throw new IllegalArgumentException("membershipDiscountAmount must be greater than or equal to 0");
        }
        if (paymentAmount < 0) {
            throw new IllegalArgumentException("paymentAmount must be greater than or equal to 0");
        }
    }

    public static class Builder {
        private List<PurchaseResultItemDto> purchaseItems;
        private List<ItemDto> freeItems;
        private int totalAmount;
        private int discountAmount;
        private int membershipDiscountAmount;
        private int paymentAmount;

        public Builder purchaseItems(List<PurchaseResultItemDto> purchaseItems) {
            this.purchaseItems = purchaseItems;
            return this;
        }

        public Builder freeItems(List<ItemDto> freeItems) {
            this.freeItems = freeItems;
            return this;
        }

        public Builder totalAmount(int totalAmount) {
            this.totalAmount = totalAmount;
            return this;
        }

        public Builder promotionDiscountAmount(int discountAmount) {
            this.discountAmount = discountAmount;
            return this;
        }

        public Builder membershipDiscountAmount(int membershipDiscountAmount) {
            this.membershipDiscountAmount = membershipDiscountAmount;
            return this;
        }

        public Builder paymentAmount(int paymentAmount) {
            this.paymentAmount = paymentAmount;
            return this;
        }

        public PurchaseResultDto build() {
            return new PurchaseResultDto(purchaseItems, freeItems, totalAmount, discountAmount, membershipDiscountAmount, paymentAmount);
        }
    }
}
