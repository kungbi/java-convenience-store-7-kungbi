package store.service.ProductStockService;

import java.util.List;
import store.dto.ProductInfoDto;
import store.dto.PurchaseItemsDto;
import store.entity.ProductStock;
import store.entity.product.Product;
import store.entity.product.ProductType;
import store.validator.ProductStockValidator;

public class ProductStockService {
    private final ProductStock productStock;
    private final ProductStockValidator productStockValidator;
    private final ProductReducer productReducer;
    private final ProductInfoService productInfoService;

    public ProductStockService(ProductStock productStock) {
        this.productStock = productStock;
        this.productStockValidator = new ProductStockValidator(productStock);
        this.productReducer = new ProductReducer(productStock, productStockValidator);
        this.productInfoService = new ProductInfoService(productStock);
    }

    // service methods

    public void validateStocks(PurchaseItemsDto purchaseItemsDto) {
        productStockValidator.validateStocks(purchaseItemsDto);
    }

    public void reduceStocks(PurchaseItemsDto purchaseItemsDto) {
        productReducer.reduceStocks(purchaseItemsDto);
    }

    public List<ProductInfoDto> getProductsInformation() {
        return productInfoService.getProductsInformation();
    }

    // productStock methods

    public Product getProduct(String name, ProductType type) {
        return productStock.getProduct(name, type);
    }

    public boolean isExistProductWithType(String name, ProductType type) {
        return productStock.isExistProductWithType(name, type);
    }

    public int getProductQuantity(String name, ProductType type) {
        return productStock.getProductQuantity(name, type);
    }

}
