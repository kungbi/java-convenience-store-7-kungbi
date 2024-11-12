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
        validatePurchaseItems(purchaseItems);
        validateFreeItems(freeItems);
        validateAmount(totalAmount, DtoExceptionMessage.TOTAL_PRICE_LESS_THAN_ZERO);
        validateAmount(promotionDiscountAmount, DtoExceptionMessage.PROMOTION_DISCOUNT_LESS_THAN_ZERO);
        validateAmount(membershipDiscountAmount, DtoExceptionMessage.MEMBERSHIP_DISCOUNT_LESS_THAN_ZERO);
        validateAmount(paymentAmount, DtoExceptionMessage.PAYMENT_AMOUNT_LESS_THAN_ZERO);
    }

    private void validatePurchaseItems(List<PurchaseResultItemDto> purchaseItems) {
        if (purchaseItems == null) {
            throw new DtoException(DtoExceptionMessage.ITEMS_NULL_OR_EMPTY);
        }
    }

    private void validateFreeItems(List<ItemDto> freeItems) {
        if (freeItems == null) {
            throw new DtoException(DtoExceptionMessage.FREE_ITEMS_NULL);
        }
    }

    private void validateAmount(int amount, DtoExceptionMessage message) {
        if (amount < 0) {
            throw new DtoException(message);
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
