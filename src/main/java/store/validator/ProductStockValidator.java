package store.validator;

import store.dto.ItemDto;
import store.dto.PurchaseItemsDto;
import store.entity.ProductStock;
import store.entity.product.Product;
import store.entity.product.ProductType;
import store.entity.product.PromotionProduct;
import store.exception.ProductStockException;
import store.exception.message.ProductStockExceptionMessage;

public class ProductStockValidator {
    private final ProductStock productStock;

    public ProductStockValidator(ProductStock productStock) {
        this.productStock = productStock;
    }

    public void validateStocks(PurchaseItemsDto purchaseItemsDto) {
        for (ItemDto productDto : purchaseItemsDto.products()) {
            validateStock(productDto);
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

}
