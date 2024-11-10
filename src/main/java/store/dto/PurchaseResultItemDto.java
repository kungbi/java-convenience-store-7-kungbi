package store.dto;

public record PurchaseResultItemDto(String name, int quantity, int price) {
    public PurchaseResultItemDto {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name must be provided");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("quantity must be greater than or equal to 0");
        }
        if (price < 0) {
            throw new IllegalArgumentException("price must be greater than or equal to 0");
        }
    }
}
