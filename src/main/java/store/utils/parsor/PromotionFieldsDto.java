package store.utils.parsor;

import java.time.LocalDateTime;

public record PromotionFieldsDto(String name, int buy, int get, LocalDateTime startDate, LocalDateTime endDate) {
    public PromotionFieldsDto {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Invalid promotion name");
        }

        if (buy <= 0) {
            throw new IllegalArgumentException("Invalid buy quantity");
        }

        if (get <= 0) {
            throw new IllegalArgumentException("Invalid get quantity");
        }

        if (startDate == null) {
            throw new IllegalArgumentException("Invalid startDate date");
        }

        if (endDate == null) {
            throw new IllegalArgumentException("Invalid endDate date");
        }
    }
}
