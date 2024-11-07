package store.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import store.entity.product.Product;
import store.entity.product.ProductType;

public class ProductInventory {
    private final Map<String, Map<ProductType, Product>> inventory;

    public ProductInventory() {
        this.inventory = new TreeMap<>();
    }

    public void addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("상품이 NULL 입니다.");
        }
        Map<ProductType, Product> products = inventory.computeIfAbsent(product.getName(), k -> new HashMap<>());
        if (products.containsKey(product.getType())) {
            throw new IllegalArgumentException("이미 존재하는 상품입니다.");
        }
        products.put(product.getType(), product.clone());
    }

    public Product getProduct(String name, ProductType type) {
        Map<ProductType, Product> productsByType = inventory.get(name);
        if (productsByType == null) {
            throw new IllegalArgumentException("존재하지 않는 상품입니다.");
        }

        Product product = productsByType.get(type);
        if (product == null) {
            throw new IllegalArgumentException("존재하지 않는 상품입니다.");
        }
        return product;
    }

}
