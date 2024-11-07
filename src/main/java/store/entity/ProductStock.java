package store.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import store.entity.product.Product;
import store.entity.product.ProductType;
import store.exception.ProductStockException;
import store.exception.message.InventoryExceptionMessage;

public class ProductStock {
    private final Map<String, Map<ProductType, Product>> inventory;

    public ProductStock() {
        this.inventory = new TreeMap<>();
    }

    public void addProduct(Product product) {
        if (product == null) {
            throw new ProductStockException(InventoryExceptionMessage.NULL_PRODUCT,
                    new NullPointerException());
        }
        Map<ProductType, Product> products = inventory.computeIfAbsent(product.getName(), k -> new HashMap<>());
        if (products.containsKey(product.getType())) {
            throw new ProductStockException(InventoryExceptionMessage.DUPLICATE_PRODUCT);
        }
        products.put(product.getType(), product.clone());
    }

    public Product getProduct(String name, ProductType type) {
        if (name == null) {
            throw new ProductStockException(InventoryExceptionMessage.NULL_NAME);
        }
        if (type == null) {
            throw new ProductStockException(InventoryExceptionMessage.NULL_TYPE);
        }

        Map<ProductType, Product> productsByType = inventory.get(name);
        if (productsByType == null) {
            throw new ProductStockException(InventoryExceptionMessage.NOT_EXIST_PRODUCT);
        }

        Product product = productsByType.get(type);
        if (product == null) {
            throw new ProductStockException(InventoryExceptionMessage.NOT_EXIST_PRODUCT);
        }
        return product;
    }

    public void reduceQuantity(String name, ProductType type, int quantity) {
        if (quantity < 0) {
            throw new ProductStockException(InventoryExceptionMessage.INVALID_QUANTITY);
        }
        Product product = getProduct(name, type);
        product.buy(quantity);
    }

}
