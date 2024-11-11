package store.service.PurchaseService;

import store.service.ProductStockService;

public class DiscountCalculator {
    private final ProductStockService productStockService;

    public DiscountCalculator(ProductStockService productStockService) {
        this.productStockService = productStockService;
    }


}
