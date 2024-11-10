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

    public void addProduct(Product product, int quantity) {
        if (product == null) {
            throw new ProductStockException(ProductStockExceptionMessage.NULL_PRODUCT,
                    new NullPointerException());
        }

        this.products.putIfAbsent(product.getName(), new HashMap<>());
        if (this.products.get(product.getName()).containsKey(product.getType())) {
            throw new ProductStockException(ProductStockExceptionMessage.DUPLICATE_PRODUCT);
        }
        this.products.get(product.getName()).put(product.getType(), product);
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
        return products.get(name).containsKey(type);
    }

    public int getProductQuantityByUuid(String uuid) {
        return stocks.get(uuid);
    }

    public boolean isSufficientStock(String name, ProductType type, int quantity) {
        if (name == null) {
            throw new ProductStockException(ProductStockExceptionMessage.NULL_NAME);
        }
        if (type == null) {
            throw new ProductStockException(ProductStockExceptionMessage.NULL_TYPE);
        }
        if (quantity <= 0) {
            throw new ProductStockException(ProductStockExceptionMessage.INVALID_QUANTITY);
        }

        Product product = getProduct(name, type);
        int currentQuantity = stocks.get(product.getUuid());
        return currentQuantity >= quantity;
    }

    public Map<ProductType, Product> getProducts(String name) {
        try {
            return products.get(name);
        } catch (NullPointerException | ClassCastException error) {
            throw new ProductStockException(ProductStockExceptionMessage.NOT_EXIST_PRODUCT, error);
        }
    }

    public Product getProduct(String name, ProductType type) {
        if (name == null) {
            throw new ProductStockException(ProductStockExceptionMessage.NULL_NAME);
        }
        if (type == null) {
            throw new ProductStockException(ProductStockExceptionMessage.NULL_TYPE);
        }
        try {
            return products.get(name).get(type);
        } catch (NullPointerException error) {
            throw new ProductStockException(ProductStockExceptionMessage.NOT_EXIST_PRODUCT, error);
        }
    }

    public int getProductQuantity(String name, ProductType type) {
        if (name == null) {
            throw new ProductStockException(ProductStockExceptionMessage.NULL_NAME);
        }
        if (type == null) {
            throw new ProductStockException(ProductStockExceptionMessage.NULL_TYPE);
        }

        Product product = getProduct(name, type);
        try {
            return stocks.get(product.getUuid());
        } catch (NullPointerException error) {
            throw new ProductStockException(ProductStockExceptionMessage.NOT_EXIST_PRODUCT, error);
        }
    }

    public void reduceProductQuantity(String name, int quantity) {
        if (name == null) {
            throw new ProductStockException(ProductStockExceptionMessage.NULL_NAME);
        }
        if (quantity <= 0) {
            throw new ProductStockException(ProductStockExceptionMessage.INVALID_QUANTITY);
        }

        if (!isExistProduct(name)) {
            throw new ProductStockException(ProductStockExceptionMessage.NOT_EXIST_PRODUCT);
        }

        Map<ProductType, Product> products = getProducts(name);

        int remainingQuantity = quantity;

        // 프로모션 상품 재고 차감
        if (products.containsKey(ProductType.PROMOTION)) {
            PromotionProduct promotionProduct = (PromotionProduct) products.get(ProductType.PROMOTION);
            if (promotionProduct.isAvailable()) {
                int promotionStock = stocks.getOrDefault(promotionProduct.getUuid(), 0);
                if (promotionStock >= remainingQuantity) {
                    stocks.put(promotionProduct.getUuid(), promotionStock - remainingQuantity);
                    return;
                }
                stocks.put(promotionProduct.getUuid(), 0);
                remainingQuantity -= promotionStock;
            }
        }

        // 일반 상품 재고 차감
        if (remainingQuantity > 0 && products.containsKey(ProductType.COMMON)) {
            Product commonProduct = products.get(ProductType.COMMON);
            int commonStock = stocks.getOrDefault(commonProduct.getUuid(), 0);
            if (commonStock >= remainingQuantity) {
                stocks.put(commonProduct.getUuid(), commonStock - remainingQuantity);
                return;
            }
            throw new ProductStockException(ProductStockExceptionMessage.INSUFFICIENT_STOCK);
        }

        // 재고 부족 시 예외 발생
        if (remainingQuantity > 0) {
            throw new ProductStockException(ProductStockExceptionMessage.INSUFFICIENT_STOCK);
        }
    }


}
