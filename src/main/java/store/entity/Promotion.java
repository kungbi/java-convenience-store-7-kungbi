package store.entity;

import java.time.LocalDateTime;
import store.exception.PromotionException;
import store.exception.message.PromotionExceptionMessage;

public class Promotion {
    private final String name;
    private final int buyQuantity;
    private final int freeQuantity;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;

    public Promotion(String name, int buyQuantity, int freeQuantity, LocalDateTime startDate,
                     LocalDateTime endDate) {
        if (name == null) {
            throw new PromotionException(PromotionExceptionMessage.NULL_NAME);
        } else if (name.isBlank()) {
            throw new PromotionException(PromotionExceptionMessage.EMPTY_NAME);
        } else if (name.length() > 50) {
            throw new PromotionException(PromotionExceptionMessage.EXCEED_NAME_LENGTH);
        }

        if (startDate == null || endDate == null) {
            throw new PromotionException(PromotionExceptionMessage.NULL_DATE);
        }

        if (buyQuantity < 1) {
            throw new PromotionException(PromotionExceptionMessage.BUY_QUANTITY_MIN);
        }
        if (freeQuantity < 1) {
            throw new PromotionException(PromotionExceptionMessage.FREE_QUANTITY_MIN);
        }

        if (startDate.isAfter(endDate)) {
            throw new PromotionException(PromotionExceptionMessage.DATE_ORDER);
        }

        this.name = name;
        this.buyQuantity = buyQuantity;
        this.freeQuantity = freeQuantity;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getName() {
        return name;
    }

    public int getBuyQuantity() {
        return buyQuantity;
    }

    public int getAdditionalFreeItemCount(int purchaseQuantity) {
        int remain = purchaseQuantity % (buyQuantity + freeQuantity);
        if (remain == buyQuantity) {
            return freeQuantity;
        }
        return 0;
    }

    public int calculateFreeCount(int quantity) {
        return quantity / (buyQuantity + freeQuantity) * freeQuantity;
    }

    public int calculateExcludedPromotionCount(int purchaseQuantity, int promotionStock) {
        purchaseQuantity = Math.min(purchaseQuantity, promotionStock);
        return purchaseQuantity % (buyQuantity + freeQuantity);
    }

    public int getFreeQuantity() {
        return freeQuantity;
    }

    public boolean isAvailable() {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(startDate) && now.isBefore(endDate);
    }
}
