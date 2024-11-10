package store.service;

import store.dto.ItemDto;
import store.dto.PurchaseItemsDto;
import store.entity.ProductStock;
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

    /**
     * 여러 상품의 재고를 검증합니다.
     *
     * @param purchaseItemsDto 구매 요청 상품 목록
     * @throws ProductStockException 재고 부족 또는 존재하지 않는 상품에 대한 예외
     */
    public void validateStocks(PurchaseItemsDto purchaseItemsDto) {
        for (ItemDto productDto : purchaseItemsDto.products()) {
            validateStock(productDto);
        }
    }

    /**
     * 여러 상품의 재고를 차감합니다.
     *
     * @param purchaseItemsDto 구매 요청 상품 목록
     * @throws ProductStockException 재고 부족 또는 존재하지 않는 상품에 대한 예외
     */
    public void reduceStocks(PurchaseItemsDto purchaseItemsDto) {
        for (ItemDto productDto : purchaseItemsDto.products()) {
            reduceStock(productDto);
        }
    }

    /**
     * 특정 상품의 재고를 검증합니다.
     *
     * @param productDto 구매 요청 상품
     * @throws ProductStockException 재고 부족 또는 존재하지 않는 상품에 대한 예외
     */
    public void validateStock(ItemDto productDto) {
        if (!productStock.isExistProduct(productDto.name())) {
            throw new ProductStockException(ProductStockExceptionMessage.NOT_EXIST_PRODUCT);
        }

        boolean isPromotionStockValid = validatePromotionStock(productDto);
        boolean isCommonStockValid = validateCommonStock(productDto);

        if (!isPromotionStockValid && !isCommonStockValid) {
            throw new ProductStockException(ProductStockExceptionMessage.INSUFFICIENT_STOCK);
        }
    }

    /**
     * 특정 상품의 재고를 차감합니다.
     *
     * @param productDto 구매 요청 상품
     * @throws ProductStockException 재고 부족 또는 존재하지 않는 상품에 대한 예외
     */
    public void reduceStock(ItemDto productDto) {
        this.validateStock(productDto);
        productStock.reduceProductQuantity(productDto.name(), productDto.quantity());
    }

    public Product getProduct(String name, ProductType type) {
        return productStock.getProduct(name, type);
    }

    public boolean isExistProductWithType(String name, ProductType type) {
        return productStock.isExistProductWithType(name, type);
    }

    public int getProductQuantity(String name, ProductType type) {
        return productStock.getProductQuantity(name, type);
    }

    // private helper methods

    private boolean validatePromotionStock(ItemDto productDto) {
        if (!productStock.isExistProductWithType(productDto.name(), ProductType.PROMOTION)) {
            return false;
        }

        if (productStock.getProduct(productDto.name(),
                ProductType.PROMOTION) instanceof PromotionProduct promotionProduct
            && !promotionProduct.getPromotion().isAvailable()) {
            return false;
        }

        if (productStock.isSufficientStock(productDto.name(), ProductType.PROMOTION, productDto.quantity())) {
            return true;
        }

        if (!productStock.isExistProductWithType(productDto.name(), ProductType.COMMON)) {
            return false;
        }

        if (productStock.getProductQuantity(productDto.name(), ProductType.PROMOTION)
            + productStock.getProductQuantity(productDto.name(), ProductType.COMMON) >= productDto.quantity()) {
            return true;
        }

        if (productStock.isSufficientStock(productDto.name(), ProductType.COMMON, productDto.quantity())) {
            return true;
        }

        return false;
    }

    private boolean validateCommonStock(ItemDto productDto) {
        if (!productStock.isExistProductWithType(productDto.name(), ProductType.COMMON)) {
            return false;
        }
        if (!productStock.isSufficientStock(productDto.name(), ProductType.COMMON, productDto.quantity())) {
            return false;
        }
        return true;
    }
}

