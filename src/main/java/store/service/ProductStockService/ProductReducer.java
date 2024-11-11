package store.service.ProductStockService;

import store.dto.ItemDto;
import store.dto.PurchaseItemsDto;
import store.entity.ProductStock;
import store.validator.ProductStockValidator;

public class ProductReducer {
    private final ProductStock productStock;
    private final ProductStockValidator productStockValidator;

    public ProductReducer(ProductStock productStock, ProductStockValidator productStockValidator) {
        this.productStock = productStock;
        this.productStockValidator = productStockValidator;
    }

    public void reduceStocks(PurchaseItemsDto purchaseItemsDto) {
        for (ItemDto productDto : purchaseItemsDto.products()) {
            reduceStock(productDto);
        }
    }

    public void reduceStock(ItemDto productDto) {
        productStockValidator.validateStock(productDto);
        productStock.reduceProductQuantity(productDto.name(), productDto.quantity());
    }

}
