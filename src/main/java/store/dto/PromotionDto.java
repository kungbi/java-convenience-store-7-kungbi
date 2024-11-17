package store.dto;

import java.time.LocalDateTime;
import store.domain.Promotion;

public record PromotionDto(String name, int buy, int get, LocalDateTime startDate, LocalDateTime endDate) {
    public PromotionDto {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }
        if (buy < 0) {
            throw new IllegalArgumentException("Buy cannot be negative");
        }
        if (get < 0) {
            throw new IllegalArgumentException("Get cannot be negative");
        }
        if (startDate == null) {
            throw new IllegalArgumentException("Start date cannot be null");
        }
        if (endDate == null) {
            throw new IllegalArgumentException("End date cannot be null");
        }
    }

    public static PromotionDto from(Promotion promotion) {
        if (promotion == null) {
            throw new IllegalArgumentException("Promotion cannot be null");
        }
        return new PromotionDto(promotion.getName(), promotion.getBuy(), promotion.getGet(), promotion.getStartDate(),
                promotion.getEndDate());
    }
}
