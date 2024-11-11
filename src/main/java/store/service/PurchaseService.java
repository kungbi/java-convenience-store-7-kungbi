package store.service;

import java.util.ArrayList;
import java.util.List;
import store.dto.ItemDto;
import store.dto.PurchaseItemsDto;
import store.dto.PurchaseRequestDto;
import store.dto.PurchaseResultDto;
import store.dto.PurchaseResultItemDto;
import store.entity.membership.Membership;
import store.entity.product.Product;
import store.entity.product.ProductType;
import store.entity.product.PromotionProduct;

public class PurchaseService {
    private final ProductStockService productStockService;
    private final PromotionService promotionService;
    private final Membership membership;

    public PurchaseService(ProductStockService productStockService, PromotionService promotionService,
                           Membership membership) {
        this.productStockService = productStockService;
        this.promotionService = promotionService;
        this.membership = membership;
    }

    public PurchaseResultDto purchase(PurchaseRequestDto purchaseInputDto) {
        productStockService.validateStocks(new PurchaseItemsDto(purchaseInputDto.products()));
        List<ItemDto> freeItems = new ArrayList<>();
        List<PurchaseResultItemDto> purchaseItems = calculatePurchaseItems(purchaseInputDto, freeItems);

        int totalAmount = calculateItemsTotalPrice(purchaseInputDto.products());
        int promotionDiscountAmount = calculatePromotionDiscountAmount(freeItems);
        int membershipDiscountAmount = calculateMembershipDiscountAmount(purchaseInputDto, totalAmount);
        this.productStockService.reduceStocks(new PurchaseItemsDto(purchaseInputDto.products()));
        return createPurchaseResultDto(purchaseItems, freeItems, promotionDiscountAmount, membershipDiscountAmount,
                totalAmount);
    }

    private int calculateMembershipDiscountAmount(PurchaseRequestDto purchaseInputDto, int totalAmount) {
        int membershipDiscountAmount = 0;
        if (purchaseInputDto.isMembership()) {
            membershipDiscountAmount = membership.applyDiscount(totalAmount);
        }
        return membershipDiscountAmount;
    }

    private Integer calculatePromotionDiscountAmount(List<ItemDto> freeItems) {
        return freeItems.stream().reduce(0, (sum, item) -> sum + productStockService.getProduct(item.name(),
                ProductType.PROMOTION).getPrice() * item.quantity(), Integer::sum);
    }

    private List<PurchaseResultItemDto> calculatePurchaseItems(PurchaseRequestDto purchaseInputDto,
                                                               List<ItemDto> freeItems) {
        List<PurchaseResultItemDto> purchaseItems = new ArrayList<>();
        for (ItemDto product : purchaseInputDto.products()) {
            calculatePurchaseItem(freeItems, product, purchaseItems);
        }
        return purchaseItems;
    }

    private void calculatePurchaseItem(List<ItemDto> freeItems, ItemDto product,
                                       List<PurchaseResultItemDto> purchaseItems) {
        if (validateProduct(product)) {
            return;
        }
        ItemDto excludedPromotionItem = promotionService.findExcludedPromotionItem(product);
        int freeCount = promotionService.calculateFreeCount(
                new ItemDto(product.name(), product.quantity() - excludedPromotionItem.quantity()));
        if (freeCount > 0) {
            freeItems.add(new ItemDto(product.name(), freeCount));
        }
        purchaseItems.add(new PurchaseResultItemDto(product.name(), product.quantity(), calculateItemPrice(product)));
    }

    private boolean validateProduct(ItemDto product) {
        return productStockService.isExistProductWithType(product.name(), ProductType.PROMOTION)
               && productStockService.getProduct(product.name(), ProductType.PROMOTION).getType()
                  != ProductType.PROMOTION;
    }

    public int calculateItemsTotalPrice(List<ItemDto> items) {
        int totalPrice = 0;
        for (ItemDto item : items) {
            totalPrice += calculateItemPrice(item);
        }
        return totalPrice;
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

    private static PurchaseResultDto createPurchaseResultDto(List<PurchaseResultItemDto> purchaseItems,
                                                             List<ItemDto> freeItems, int promotionDiscountAmount,
                                                             int membershipDiscountAmount, int totalAmount) {
        return new PurchaseResultDto.Builder().purchaseItems(purchaseItems).freeItems(freeItems)
                .promotionDiscountAmount(promotionDiscountAmount).membershipDiscountAmount(membershipDiscountAmount)
                .totalAmount(totalAmount)
                .paymentAmount(totalAmount - promotionDiscountAmount - membershipDiscountAmount).build();
    }


}
