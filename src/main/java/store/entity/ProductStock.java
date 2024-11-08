package store.entity;

import java.util.HashMap;
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

    public void addProducts(Product product, int quantity) {
        if (product == null) {
            throw new ProductStockException(ProductStockExceptionMessage.NULL_PRODUCT,
                    new NullPointerException());
        }
        Map<ProductType, Product> products = this.products.computeIfAbsent(product.getName(), k -> new HashMap<>());
        if (products.containsKey(product.getType())) {
            throw new ProductStockException(ProductStockExceptionMessage.DUPLICATE_PRODUCT);
        }
        products.put(product.getType(), product);
        stocks.put(product.getUuid(), quantity);
    }

    public int getProductQuantity(String name, ProductType type) {
        try {
            Product product = products.get(name).get(type);
            return stocks.get(product.getUuid());
        } catch (NullPointerException error) {
            throw new ProductStockException(ProductStockExceptionMessage.NOT_EXIST_PRODUCT, error);
        }
    }

    public void reduceProductQuantity(String name, ProductType type, int quantity) {
        Product product = products.get(name).get(type);
        int currentQuantity = stocks.get(product.getUuid());
        if (currentQuantity < quantity) {
            throw new ProductStockException(ProductStockExceptionMessage.INSUFFICIENT_STOCK);
        }
        stocks.put(product.getUuid(), currentQuantity - quantity);
    }

}
