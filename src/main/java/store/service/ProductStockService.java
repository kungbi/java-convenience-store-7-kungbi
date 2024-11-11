package store.service;

import java.util.ArrayList;
import java.util.List;
import store.dto.ItemDto;
import store.dto.ProductInfoDto;
import store.dto.PurchaseItemsDto;
import store.entity.ProductStock;
import store.entity.Promotion;
import store.entity.product.Product;
import store.entity.product.ProductType;
import store.entity.product.PromotionProduct;
import store.exception.ProductStockException;
import store.exception.message.ProductStockExceptionMessage;

public class ProductStockService {
    private final ProductStock productStock;

    public ProductStockService(ProductStock productStock) {
        this.productStock = productStock;
    }

    public void validateStocks(PurchaseItemsDto purchaseItemsDto) {
        for (ItemDto productDto : purchaseItemsDto.products()) {
            validateStock(productDto);
        }
    }

    public void reduceStocks(PurchaseItemsDto purchaseItemsDto) {
        for (ItemDto productDto : purchaseItemsDto.products()) {
            reduceStock(productDto);
        }
    }

    public void validateStock(ItemDto productDto) {
        if (!productStock.isExistProduct(productDto.name())) {
            throw new ProductStockException(ProductStockExceptionMessage.NOT_EXIST_PRODUCT);
        }

        if (!isStockAvailable(productDto)) {
            throw new ProductStockException(ProductStockExceptionMessage.INSUFFICIENT_STOCK);
        }
    }

    public void reduceStock(ItemDto productDto) {
        validateStock(productDto);
        productStock.reduceProductQuantity(productDto.name(), productDto.quantity());
    }

    public Product getProduct(String name, ProductType type) {
        return productStock.getProduct(name, type);
    }

    public List<ProductInfoDto> getProductsInformation() {
        List<ProductInfoDto> productInfoDtos = new ArrayList<>();
        for (Product product : productStock.getProducts()) {
            addProductInfoDto(productInfoDtos, product);
        }
        return productInfoDtos;
    }

    public boolean isExistProductWithType(String name, ProductType type) {
        return productStock.isExistProductWithType(name, type);
    }

    public int getProductQuantity(String name, ProductType type) {
        return productStock.getProductQuantity(name, type);
    }

    // Private helper methods

    private boolean isStockAvailable(ItemDto productDto) {
        return validatePromotionStock(productDto) || validateCommonStock(productDto);
    }

    private boolean validatePromotionStock(ItemDto productDto) {
        if (!productStock.isExistProductWithType(productDto.name(), ProductType.PROMOTION)) {
            return false;
        }
        if (!isPromotionAvailable(productDto)) {
            return false;
        }
        return productStock.isSufficientStock(productDto.name(), ProductType.PROMOTION, productDto.quantity())
               || isCombinedStockSufficient(productDto);
    }

    private boolean isPromotionAvailable(ItemDto productDto) {
        Product promotionProduct = productStock.getProduct(productDto.name(), ProductType.PROMOTION);
        return ((PromotionProduct) promotionProduct).getPromotion().isAvailable();
    }

    private boolean isCombinedStockSufficient(ItemDto productDto) {
        int promotionStock = productStock.getProductQuantity(productDto.name(), ProductType.PROMOTION);
        int commonStock = productStock.getProductQuantity(productDto.name(), ProductType.COMMON);
        return promotionStock + commonStock >= productDto.quantity();
    }

    private boolean validateCommonStock(ItemDto productDto) {
        return productStock.isExistProductWithType(productDto.name(), ProductType.COMMON)
               && productStock.isSufficientStock(productDto.name(), ProductType.COMMON, productDto.quantity());
    }

    private void addProductInfoDto(List<ProductInfoDto> productInfoDtos, Product product) {
        int quantity = productStock.getProductQuantityByUuid(product.getUuid());
        String promotionName = getPromotionNameIfApplicable(product);
        productInfoDtos.add(new ProductInfoDto(product.getName(), product.getPrice(), quantity, promotionName));
    }

    private String getPromotionNameIfApplicable(Product product) {
        if (product.getType() == ProductType.PROMOTION) {
            Promotion promotion = ((PromotionProduct) product).getPromotion();
            return promotion.getName();
        }
        return null;
    }
}
