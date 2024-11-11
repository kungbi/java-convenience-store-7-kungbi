package store.service.PromotionService;

import store.dto.AdditionalFreeItemsDto;
import store.dto.ExcludedPromotionItemsDto;
import store.dto.ItemDto;
import store.dto.PurchaseItemsDto;
import store.entity.ProductStock;
import store.entity.Promotion;
import store.entity.product.ProductType;
import store.entity.product.PromotionProduct;
import store.validator.PromotionValidator;

public class PromotionService {
    private final ProductStock productStock;
    private final PromotionValidator promotionValidator;
    private final ExcludedPromotionItemsCalculator excludedPromotionItemsCalculator;
    private final AdditionalFreeItemsCalculator additionalFreeItemsCalculator;

    public PromotionService(ProductStock productStock) {
        this.productStock = productStock;
        this.promotionValidator = new PromotionValidator(productStock);
        this.excludedPromotionItemsCalculator = new ExcludedPromotionItemsCalculator(productStock, promotionValidator);
        this.additionalFreeItemsCalculator = new AdditionalFreeItemsCalculator(productStock, promotionValidator);
    }

    public AdditionalFreeItemsDto findAdditionalFreeItems(PurchaseItemsDto purchaseItemsDto) {
        return additionalFreeItemsCalculator.findAdditionalFreeItems(purchaseItemsDto);
    }

    public ExcludedPromotionItemsDto findExcludedPromotionItems(PurchaseItemsDto purchaseItemsDto) {
        return excludedPromotionItemsCalculator.findExcludedPromotionItems(purchaseItemsDto);
    }

    public ItemDto findExcludedPromotionItem(ItemDto product) {
        return excludedPromotionItemsCalculator.findExcludedPromotionItem(product);
    }

    public int calculateFreeCount(ItemDto purchaseItemDto) {
        Promotion promotion = promotionValidator.getValidPromotion(purchaseItemDto.name());
        if (promotion == null) {
            return 0;
        }
        int freeCount = promotion.calculateFreeCount(purchaseItemDto.quantity());
        int promotionStock = productStock.getProductQuantityByUuid(
                ((PromotionProduct) productStock.getProduct(purchaseItemDto.name(), ProductType.PROMOTION)).getUuid());
        return Math.min(freeCount, promotionStock);
    }

}
