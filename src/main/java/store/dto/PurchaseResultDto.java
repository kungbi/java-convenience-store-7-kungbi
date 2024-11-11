package store.dto;


import java.util.List;
import store.exception.DtoException;
import store.exception.message.DtoExceptionMessage;

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
            throw new DtoException(DtoExceptionMessage.ITEMS_NULL_OR_EMPTY);
        }
        if (freeItems == null) {
            throw new DtoException(DtoExceptionMessage.FREE_ITEMS_NULL);
        }
        if (totalAmount < 0) {
            throw new DtoException(DtoExceptionMessage.TOTAL_PRICE_LESS_THAN_ZERO);
        }
        if (promotionDiscountAmount < 0) {
            throw new DtoException(DtoExceptionMessage.PROMOTION_DISCOUNT_LESS_THAN_ZERO);
        }
        if (membershipDiscountAmount < 0) {
            throw new DtoException(DtoExceptionMessage.MEMBERSHIP_DISCOUNT_LESS_THAN_ZERO);
        }
        if (paymentAmount < 0) {
            throw new DtoException(DtoExceptionMessage.PAYMENT_AMOUNT_LESS_THAN_ZERO);
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
            return new PurchaseResultDto(purchaseItems, freeItems, totalAmount, discountAmount,
                    membershipDiscountAmount, paymentAmount);
        }
    }
}
