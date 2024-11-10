package store.utils.parsor;

public record ProductFieldsDto(String name, int price, int quantity, String promotionName) {
    public ProductFieldsDto {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Invalid product name");
        }

        if (price <= 0) {
            throw new IllegalArgumentException("Invalid product price");
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException("Invalid product quantity");
        }
    }
}
