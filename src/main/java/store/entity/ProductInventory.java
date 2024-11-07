package store.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import store.entity.product.Product;
import store.entity.product.ProductType;
import store.exception.ProductInventoryException;
import store.exception.message.ProductInventoryExceptionMessage;

public class ProductInventory {
    private final Map<String, Map<ProductType, Product>> inventory;

    public ProductInventory() {
        this.inventory = new TreeMap<>();
    }

    public void addProduct(Product product) {
        if (product == null) {
            throw new ProductInventoryException(ProductInventoryExceptionMessage.NULL_PRODUCT,
                    new NullPointerException());
        }
        Map<ProductType, Product> products = inventory.computeIfAbsent(product.getName(), k -> new HashMap<>());
        if (products.containsKey(product.getType())) {
            throw new ProductInventoryException(ProductInventoryExceptionMessage.DUPLICATE_PRODUCT);
        }
        products.put(product.getType(), product.clone());
    }

    public Product getProduct(String name, ProductType type) {
        Map<ProductType, Product> productsByType = inventory.get(name);
        if (productsByType == null) {
            throw new ProductInventoryException(ProductInventoryExceptionMessage.NOT_EXIST_PRODUCT);
        }

        Product product = productsByType.get(type);
        if (product == null) {
            throw new ProductInventoryException(ProductInventoryExceptionMessage.NOT_EXIST_PRODUCT);
        }
        return product;
    }

}
