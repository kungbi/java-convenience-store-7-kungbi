package store.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import store.entity.product.Product;
import store.entity.product.ProductType;
import store.exception.InventoryException;
import store.exception.message.InventoryExceptionMessage;

public class Inventory {
    private final Map<String, Map<ProductType, Product>> inventory;

    public Inventory() {
        this.inventory = new TreeMap<>();
    }

    public void addProduct(Product product) {
        if (product == null) {
            throw new InventoryException(InventoryExceptionMessage.NULL_PRODUCT,
                    new NullPointerException());
        }
        Map<ProductType, Product> products = inventory.computeIfAbsent(product.getName(), k -> new HashMap<>());
        if (products.containsKey(product.getType())) {
            throw new InventoryException(InventoryExceptionMessage.DUPLICATE_PRODUCT);
        }
        products.put(product.getType(), product.clone());
    }

    public Product getProduct(String name, ProductType type) {
        if (name == null) {
            throw new InventoryException(InventoryExceptionMessage.NULL_NAME);
        }
        if (type == null) {
            throw new InventoryException(InventoryExceptionMessage.NULL_TYPE);
        }

        Map<ProductType, Product> productsByType = inventory.get(name);
        if (productsByType == null) {
            throw new InventoryException(InventoryExceptionMessage.NOT_EXIST_PRODUCT);
        }

        Product product = productsByType.get(type);
        if (product == null) {
            throw new InventoryException(InventoryExceptionMessage.NOT_EXIST_PRODUCT);
        }
        return product;
    }

}
