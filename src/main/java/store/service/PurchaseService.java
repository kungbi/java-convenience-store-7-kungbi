package store.service;

import java.util.ArrayList;
import java.util.List;
import store.dto.ItemDto;
import store.dto.PurchaseItemsDto;
import store.dto.PurchaseRequestDto;
import store.dto.PurchaseResultDto;
import store.entity.Promotion;
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
        productStockService.validateStocks(new PurchaseItemsDto(
                purchaseInputDto.products()
        ));

        List<ItemDto> freeItems = new ArrayList<>();
        for (ItemDto product : purchaseInputDto.products()) {
            if (productStockService.getProduct(product.name(), ProductType.PROMOTION).getType()
                != ProductType.PROMOTION) {
                continue;
            }

            ItemDto excludedPromotionItem = promotionService.findExcludedPromotionItem(product);
            int freeCount = promotionService.calculateFreeCount(
                    new ItemDto(product.name(), product.quantity() - excludedPromotionItem.quantity()));
            if (freeCount > 0) {
                freeItems.add(new ItemDto(product.name(), freeCount));
            }
        }

        int totalAmount = calculateItemsTotalPrice(purchaseInputDto.products());
        int promotionDiscountAmount = freeItems.stream()
                .reduce(0, (sum, item) -> sum +
                                          productStockService.getProduct(item.name(), ProductType.PROMOTION).getPrice()
                                          * item.quantity(), Integer::sum);
        int membershipDiscountAmount = 0;
        if (purchaseInputDto.isMembership()) {
            membershipDiscountAmount = membership.applyDiscount(totalAmount);
        }

        this.productStockService.reduceStocks(new PurchaseItemsDto(
                purchaseInputDto.products()
        ));

        return new PurchaseResultDto.Builder()
                .purchaseItems(purchaseInputDto.products())
                .freeItems(freeItems)
                .promotionDiscountAmount(promotionDiscountAmount)
                .membershipDiscountAmount(membershipDiscountAmount)
                .totalAmount(totalAmount)
                .paymentAmount(totalAmount - promotionDiscountAmount - membershipDiscountAmount)
                .build();
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

        // 프로모션 상품과 일반 상품의 가격을 가져옵니다.
        int promotionPrice = 0;
        int commonPrice = 0;

        // 프로모션 상품이 존재하고 적용 가능한지 확인
        if (productStockService.isExistProductWithType(productName, ProductType.PROMOTION)) {
            Product promotionProduct = productStockService.getProduct(productName, ProductType.PROMOTION);

            // 프로모션이 적용 가능한 경우에만 프로모션 가격 설정
            if (promotionProduct instanceof PromotionProduct promoProduct) {
                Promotion promotion = promoProduct.getPromotion();
                if (promotion.isAvailable()) {
                    promotionPrice = promoProduct.getPrice();
                }
            }
        }

        // 일반 상품 가격 설정
        if (productStockService.isExistProductWithType(productName, ProductType.COMMON)) {
            Product commonProduct = productStockService.getProduct(productName, ProductType.COMMON);
            commonPrice = commonProduct.getPrice();
        }

        // 사용할 수 있는 프로모션 수량 확인 (프로모션 가격이 설정된 경우에만)
        int availablePromotionStock = 0;
        if (promotionPrice > 0) {
            availablePromotionStock = productStockService.getProductQuantity(productName, ProductType.PROMOTION);
        }

        // 프로모션 상품과 일반 상품의 수량을 계산
        int promotionQuantity = Math.min(purchaseQuantity, availablePromotionStock);
        int commonQuantity = purchaseQuantity - promotionQuantity;

        // 최종 가격 계산
        return (promotionQuantity * promotionPrice) + (commonQuantity * commonPrice);
    }


}
