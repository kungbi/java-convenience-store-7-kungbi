package store.service;

import store.entity.ProductStock;

public class PurchaseService {
    private final ProductStock productStock;


    public PurchaseService(ProductStock productStock) {
        this.productStock = productStock;
    }
}
