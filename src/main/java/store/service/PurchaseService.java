package store.service;

import store.dto.PurchaseInputDto;
import store.entity.ProductStock;

public class PurchaseService {
    private final ProductStock productStock;


    public PurchaseService(ProductStock productStock) {
        this.productStock = productStock;
    }

    public void purchase(PurchaseInputDto inputDto) {
        // 구매 로직 구현
    }
}
