package store.dto;

import java.util.Optional;
import store.config.ProductType;
import store.domain.Product;

public record ProductDto(String name, int price, ProductType type, int quantity, Optional<PromotionDto> promotion) {
    public ProductDto {
        if (name == null) {
            throw new IllegalArgumentException("Product name cannot be null");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Product price cannot be negative");
        }
        if (type == null) {
            throw new IllegalArgumentException("Product type cannot be null");
        }
    }

    public static ProductDto of(Product product, int quantity, PromotionDto promotion) {
        return new ProductDto(
                product.getName(),
                product.getPrice(),
                product.getType(),
                quantity,
                Optional.ofNullable(promotion)
        );
    }

}
