package store.service;

import java.util.ArrayList;
import java.util.List;
import store.dto.ItemDto;
import store.dto.PurchaseItemsDto;
import store.dto.PurchaseRequestDto;
import store.dto.PurchaseResultDto;
import store.entity.membership.Membership;
import store.entity.product.Product;
import store.entity.product.ProductType;

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
            // 증정 상품 계산
            // 1. 프로모션이고, 적용 가능하다면 프로모션 증정 상품 추가

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

            // 상품 재고 차감
//            this.productStockService.reduceStock(product);
        }

        // 총 구매 금액 계산
        int totalAmount = calculateItemsTotalPrice(purchaseInputDto.products());
//        totalAmount = 2000;

        // 할인 금액 계산 (증정상품)
        // 1. 증정 상품의 가격 * 수량 합산
        int promotionDiscountAmount = freeItems.stream()
                .reduce(0, (sum, item) -> sum +
                                          productStockService.getProduct(item.name(), ProductType.PROMOTION).getPrice()
                                          * item.quantity(), Integer::sum);

        // 멤버십 할인 금액 계산
        // 1. 총 구매 금액 * 멤버십 할인율
        int membershipDiscountAmount = 0;
        if (purchaseInputDto.isMembership()) {
            membershipDiscountAmount = membership.applyDiscount(totalAmount);
        }

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

        Product promotionProduct = null;
        Product commonProduct = null;

        if (productStockService.isExistProductWithType(productName, ProductType.PROMOTION)) {
            promotionProduct = productStockService.getProduct(productName, ProductType.PROMOTION);
            promotionPrice = promotionProduct.getPrice();
        }

        if (productStockService.isExistProductWithType(productName, ProductType.COMMON)) {
            commonProduct = productStockService.getProduct(productName, ProductType.COMMON);
            commonPrice = commonProduct.getPrice();
        }

        // 사용할 수 있는 프로모션 수량과 일반 상품 수량을 확인합니다.
        int availablePromotionStock = 0;
        if (promotionProduct != null) {
            availablePromotionStock = productStockService.getProductQuantity(productName, ProductType.PROMOTION);
        }

        int promotionQuantity;
        int commonQuantity;

        if (purchaseQuantity <= availablePromotionStock) {
            promotionQuantity = purchaseQuantity;
            commonQuantity = 0;
        } else {
            promotionQuantity = availablePromotionStock;
            commonQuantity = purchaseQuantity - availablePromotionStock;
        }

        // 최종 가격 계산
        int totalPrice = (promotionQuantity * promotionPrice) + (commonQuantity * commonPrice);
        return totalPrice;
    }
}
