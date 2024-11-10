package store.dto;

public record ItemDto(String name, int quantity) {
    public ItemDto {
        if (name == null) {
            throw new IllegalArgumentException("name must be provided");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("quantity must be greater than 0");
        }
    }
}
