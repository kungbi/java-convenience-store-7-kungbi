package store.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import store.entity.product.Product;
import store.entity.product.ProductType;
import store.entity.product.PromotionProduct;
import store.exception.ProductStockException;
import store.exception.message.ProductStockExceptionMessage;

public class ProductStock {
    private final Map<String, Map<ProductType, Product>> products;
    private final Map<String, Integer> stocks;

    public ProductStock() {
        this.products = new HashMap<>();
        this.stocks = new HashMap<>();
    }

    // public methods

    public void addProduct(Product product, int quantity) {
        validateProduct(product);
        products.putIfAbsent(product.getName(), new HashMap<>());
        checkDuplicateProduct(product);
        products.get(product.getName()).put(product.getType(), product);
        stocks.put(product.getUuid(), quantity);
    }

    public List<Product> getProducts() {
        return products.values().stream()
                .flatMap(map -> map.values().stream())
                .toList();
    }

    public boolean isExistProduct(String name) {
        return products.containsKey(name);
    }

    public boolean isExistProductWithType(String name, ProductType type) {
        return products.getOrDefault(name, Map.of()).containsKey(type);
    }

    public int getProductQuantityByUuid(String uuid) {
        return stocks.getOrDefault(uuid, 0);
    }

    public boolean isSufficientStock(String name, ProductType type, int quantity) {
        validateNameAndType(name, type);
        int currentQuantity = getProductQuantity(name, type);
        return currentQuantity >= quantity;
    }

    public Product getProduct(String name, ProductType type) {
        validateNameAndType(name, type);
        return products.getOrDefault(name, Map.of()).get(type);
    }

    public int getProductQuantity(String name, ProductType type) {
        Product product = getProduct(name, type);
        try {
            return stocks.get(product.getUuid());
        } catch (NullPointerException error) {
            throw new ProductStockException(ProductStockExceptionMessage.NOT_EXIST_PRODUCT);
        }
    }

    public void reduceProductQuantity(String name, int quantity) {
        validateReduceRequest(name, quantity);
        Map<ProductType, Product> products = this.products.get(name);

        int remainingQuantity = quantity;
        if (products.containsKey(ProductType.PROMOTION)) {
            PromotionProduct promotionProduct = (PromotionProduct) products.get(ProductType.PROMOTION);
            remainingQuantity = reducePromotionStock(promotionProduct, quantity);
        }
        reduceCommonStock(products, remainingQuantity);
    }

    // Private helper methods

    private void validateProduct(Product product) {
        if (product == null) {
            throw new ProductStockException(ProductStockExceptionMessage.NULL_PRODUCT, new NullPointerException());
        }
    }

    private void checkDuplicateProduct(Product product) {
        if (products.get(product.getName()).containsKey(product.getType())) {
            throw new ProductStockException(ProductStockExceptionMessage.DUPLICATE_PRODUCT);
        }
    }

    private void validateNameAndType(String name, ProductType type) {
        if (name == null) {
            throw new ProductStockException(ProductStockExceptionMessage.NULL_NAME);
        }
        if (type == null) {
            throw new ProductStockException(ProductStockExceptionMessage.NULL_TYPE);
        }
    }

    private void validateReduceRequest(String name, int quantity) {
        if (name == null || quantity <= 0) {
            throw new ProductStockException(ProductStockExceptionMessage.INVALID_QUANTITY);
        }
        if (!isExistProduct(name)) {
            throw new ProductStockException(ProductStockExceptionMessage.NOT_EXIST_PRODUCT);
        }
    }

    private int reducePromotionStock(PromotionProduct promotionProduct, int quantity) {
        if (!promotionProduct.isAvailable()) {
            return quantity;
        }

        int promoStock = stocks.getOrDefault(promotionProduct.getUuid(), 0);
        int usedQuantity = Math.min(promoStock, quantity);
        stocks.put(promotionProduct.getUuid(), promoStock - usedQuantity);
        return quantity - usedQuantity;
    }

    private void reduceCommonStock(Map<ProductType, Product> productTypes, int quantity) {
        if (quantity == 0 || !productTypes.containsKey(ProductType.COMMON)) {
            return;
        }

        Product commonProduct = productTypes.get(ProductType.COMMON);
        int commonStock = stocks.getOrDefault(commonProduct.getUuid(), 0);
        if (commonStock < quantity) {
            throw new ProductStockException(ProductStockExceptionMessage.INSUFFICIENT_STOCK);
        }
        stocks.put(commonProduct.getUuid(), commonStock - quantity);
    }
}
