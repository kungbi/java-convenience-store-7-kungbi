package store.service.PurchaseService;

import java.util.List;
import store.dto.ItemDto;
import store.entity.product.Product;
import store.entity.product.ProductType;
import store.entity.product.PromotionProduct;
import store.service.ProductStockService;

public class PriceCalculator {
    private final ProductStockService productStockService;


    public PriceCalculator(ProductStockService productStockService) {
        this.productStockService = productStockService;
    }


    public int calculateItemPrice(ItemDto item) {
        String productName = item.name();

        int purchaseQuantity = item.quantity();
        int promotionPrice = getPromotionPrice(productName);
        int commonPrice = getCommonPrice(productName);
        int availablePromotionStock = getAvailablePromotionStock(productName, promotionPrice);
        int promotionQuantity = calculatePromotionQuantity(purchaseQuantity, availablePromotionStock);
        int commonQuantity = calculateCommonQuantity(purchaseQuantity, promotionQuantity);

        return calculateTotalPrice(promotionQuantity, promotionPrice, commonQuantity, commonPrice);
    }


    private int getPromotionPrice(String productName) {
        if (productStockService.isExistProductWithType(productName, ProductType.PROMOTION)) {
            Product promotionProduct = productStockService.getProduct(productName, ProductType.PROMOTION);
            if (promotionProduct instanceof PromotionProduct promoProduct && promoProduct.getPromotion()
                    .isAvailable()) {
                return promoProduct.getPrice();
            }
        }
        return 0;
    }

    public int calculateItemsTotalPrice(List<ItemDto> items) {
        int totalPrice = 0;
        for (ItemDto item : items) {
            totalPrice += calculateItemPrice(item);
        }
        return totalPrice;
    }


    // private method

    private int getCommonPrice(String productName) {
        if (productStockService.isExistProductWithType(productName, ProductType.COMMON)) {
            Product commonProduct = productStockService.getProduct(productName, ProductType.COMMON);
            return commonProduct.getPrice();
        }
        return 0;
    }

    private int getAvailablePromotionStock(String productName, int promotionPrice) {
        if (promotionPrice > 0) {
            return productStockService.getProductQuantity(productName, ProductType.PROMOTION);
        }
        return 0;
    }

    private int calculatePromotionQuantity(int purchaseQuantity, int availablePromotionStock) {
        return Math.min(purchaseQuantity, availablePromotionStock);
    }

    private int calculateCommonQuantity(int purchaseQuantity, int promotionQuantity) {
        return purchaseQuantity - promotionQuantity;
    }

    private int calculateTotalPrice(int promotionQuantity, int promotionPrice, int commonQuantity, int commonPrice) {
        return (promotionQuantity * promotionPrice) + (commonQuantity * commonPrice);
    }
}
