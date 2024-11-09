package store.service;

import store.dto.PurchaseItemDto;
import store.dto.PurchaseItemsDto;
import store.entity.ProductStock;
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
        for (PurchaseItemDto productDto : purchaseItemsDto.products()) {
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
        for (PurchaseItemDto productDto : purchaseItemsDto.products()) {
            reduceStock(productDto);
        }
    }

    /**
     * 특정 상품의 재고를 검증합니다.
     *
     * @param productDto 구매 요청 상품
     * @throws ProductStockException 재고 부족 또는 존재하지 않는 상품에 대한 예외
     */
    public void validateStock(PurchaseItemDto productDto) {
        if (!productStock.isExistProduct(productDto.name())) {
            throw new ProductStockException(ProductStockExceptionMessage.NOT_EXIST_PRODUCT);
        }

        boolean isPromotionStockValid = validatePromotionStock(productDto);
        boolean isCommonStockValid = validateCommonStock(productDto);

        if (!isPromotionStockValid || !isCommonStockValid) {
            throw new ProductStockException(ProductStockExceptionMessage.INSUFFICIENT_STOCK);
        }
    }

    /**
     * 특정 상품의 재고를 차감합니다.
     *
     * @param productDto 구매 요청 상품
     * @throws ProductStockException 재고 부족 또는 존재하지 않는 상품에 대한 예외
     */
    public void reduceStock(PurchaseItemDto productDto) {
        this.validateStock(productDto);
        productStock.reduceProductQuantity(productDto.name(), productDto.quantity());
    }

    // private helper methods

    private boolean validatePromotionStock(PurchaseItemDto productDto) {
        if (!productStock.isExistProductWithType(productDto.name(), ProductType.PROMOTION)) {
            return true;
        }

        PromotionProduct promotionProduct = (PromotionProduct) productStock.getProduct(productDto.name(),
                ProductType.PROMOTION);
        if (!promotionProduct.getPromotion().isAvailable()) {
            return true;
        }

        return productStock.isSufficientStock(productDto.name(), ProductType.PROMOTION, productDto.quantity()) ||
               (productStock.isExistProductWithType(productDto.name(), ProductType.COMMON) &&
                productStock.isSufficientStock(productDto.name(), ProductType.COMMON, productDto.quantity()));
    }

    private boolean validateCommonStock(PurchaseItemDto productDto) {
        return !productStock.isExistProductWithType(productDto.name(), ProductType.COMMON) ||
               productStock.isSufficientStock(productDto.name(), ProductType.COMMON, productDto.quantity());
    }
}

