package store.service;

import java.util.List;
import java.util.Optional;
import store.config.ProductType;
import store.domain.Product;
import store.domain.Stock;
import store.repository.ProductPromotionRepository;
import store.repository.ProductRepository;
import store.repository.PromotionRepository;

public class StockService {
    private final Stock stock;
    private final ProductRepository productRepository;
    private final PromotionRepository promotionRepository;
    private final ProductPromotionRepository productPromotionRepository;

    public StockService(Stock stock, ProductRepository productRepository, PromotionRepository promotionRepository,
                        ProductPromotionRepository productPromotionRepository) {
        this.stock = stock;
        this.productRepository = productRepository;
        this.promotionRepository = promotionRepository;
        this.productPromotionRepository = productPromotionRepository;
    }

    public boolean isAvailableToBuy(String productName, int quantity) {
        Optional<List<Product>> products = productRepository.findByName(productName);
        if (products.isEmpty()) {
            throw new IllegalArgumentException("Product not found");
        }

        int totalStock = 0;
        for (Product product : products.get()) {
            totalStock += stock.getQuantity(product.getId());
        }

        return totalStock >= quantity;
    }

    public void reduceStock(String productName, int quantity) {
        if (!this.isAvailableToBuy(productName, quantity)) {
            throw new IllegalArgumentException("Not enough stock");
        }
        int remainQuantity = quantity;

        remainQuantity = this.reducePromotionStock(productName, remainQuantity);
        remainQuantity = this.reduceCommonStock(productName, remainQuantity);
        if (0 < remainQuantity) {
            throw new IllegalStateException("Not enough stock");
        }
    }

    private int reducePromotionStock(String productName, int quantity) {
        Optional<Product> optionalProduct = productRepository.findByNameAndType(productName, ProductType.PROMOTION);
        if (optionalProduct.isEmpty()) {
            return quantity;
        }

        Product product = optionalProduct.get();
        int decreaseQuantity = Math.min(stock.getQuantity(product.getId()), quantity);
        stock.decrease(product.getId(), decreaseQuantity);
        return quantity - decreaseQuantity;
    }

    private int reduceCommonStock(String productName, int quantity) {
        Optional<Product> optionalProduct = productRepository.findByNameAndType(productName, ProductType.COMMON);
        if (optionalProduct.isEmpty()) {
            return quantity;
        }

        Product product = optionalProduct.get();
        int decreaseQuantity = Math.min(stock.getQuantity(product.getId()), quantity);
        stock.decrease(product.getId(), decreaseQuantity);
        return quantity - decreaseQuantity;
    }


}
