package store.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import store.entity.product.Product;
import store.entity.product.ProductType;
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

    public Map<ProductType, Product> getProducts(String name) {
        try {
            return products.get(name);
        } catch (NullPointerException error) {
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

    public void reduceProductQuantity(String name, ProductType type, int quantity) {
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
        if (currentQuantity < quantity) {
            throw new ProductStockException(ProductStockExceptionMessage.INSUFFICIENT_STOCK);
        }
        stocks.put(product.getUuid(), currentQuantity - quantity);
    }

}
