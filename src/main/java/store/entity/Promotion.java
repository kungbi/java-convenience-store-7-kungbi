package store.entity;

import java.time.LocalDateTime;
import store.exception.PromotionException;
import store.exception.message.PromotionExceptionMessage;
import store.utils.Date.LocalDateTimes;

public class Promotion {
    private final String name;
    private final int buyQuantity;
    private final int freeQuantity;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;

    public Promotion(String name, int buyQuantity, int freeQuantity, LocalDateTime startDate, LocalDateTime endDate) {
        validate(name, buyQuantity, freeQuantity, startDate, endDate);
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

    public int getFreeQuantity() {
        return freeQuantity;
    }

    public int calculateFreeCount(int quantity) {
        return (quantity / (buyQuantity + freeQuantity)) * freeQuantity;
    }

    public int calculateExcludedPromotionCount(int purchaseQuantity, int promotionStock) {
        int adjustedQuantity = Math.min(purchaseQuantity, promotionStock);
        return adjustedQuantity % (buyQuantity + freeQuantity);
    }

    public int getAdditionalFreeItemCount(int purchaseQuantity) {
        if ((purchaseQuantity % (buyQuantity + freeQuantity) == buyQuantity)) {
            return freeQuantity;
        }
        return 0;
    }

    public boolean isAvailable() {
        LocalDateTime now = LocalDateTimes.now();
        return now.isAfter(startDate) && now.isBefore(endDate);
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    // Private helper methods for validation

    private void validate(String name, int buyQuantity, int freeQuantity, LocalDateTime startDate,
                          LocalDateTime endDate) {
        validateName(name);
        validateDateRange(startDate, endDate);
        validateQuantities(buyQuantity, freeQuantity);
    }

    private void validateName(String name) {
        if (name == null) {
            throw new PromotionException(PromotionExceptionMessage.NULL_NAME);
        }
        if (name.isBlank()) {
            throw new PromotionException(PromotionExceptionMessage.EMPTY_NAME);
        }
        if (name.length() > 50) {
            throw new PromotionException(PromotionExceptionMessage.EXCEED_NAME_LENGTH);
        }
    }

    private void validateDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null || endDate == null) {
            throw new PromotionException(PromotionExceptionMessage.NULL_DATE);
        }
        if (startDate.isAfter(endDate)) {
            throw new PromotionException(PromotionExceptionMessage.DATE_ORDER);
        }
    }

    private void validateQuantities(int buyQuantity, int freeQuantity) {
        if (buyQuantity < 1) {
            throw new PromotionException(PromotionExceptionMessage.BUY_QUANTITY_MIN);
        }
        if (freeQuantity < 1) {
            throw new PromotionException(PromotionExceptionMessage.FREE_QUANTITY_MIN);
        }
    }
}
