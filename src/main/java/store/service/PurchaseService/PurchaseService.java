package store.service.PurchaseService;

import java.util.ArrayList;
import java.util.List;
import store.dto.ItemDto;
import store.dto.PurchaseItemsDto;
import store.dto.PurchaseRequestDto;
import store.dto.PurchaseResultDto;
import store.dto.PurchaseResultItemDto;
import store.entity.membership.Membership;
import store.entity.product.ProductType;
import store.service.ProductStockService;
import store.service.PromotionService;

public class PurchaseService {
    private final ProductStockService productStockService;
    private final PromotionService promotionService;
    private final Membership membership;
    private final PriceCalculator priceCalculator;
    private final DiscountCalculator discountCalculator;

    public PurchaseService(ProductStockService productStockService, PromotionService promotionService,
                           Membership membership) {
        this.productStockService = productStockService;
        this.promotionService = promotionService;
        this.membership = membership;
        this.priceCalculator = new PriceCalculator(productStockService);
        this.discountCalculator = new DiscountCalculator(membership, productStockService);
    }

    public PurchaseResultDto purchase(PurchaseRequestDto purchaseInputDto) {
        productStockService.validateStocks(new PurchaseItemsDto(purchaseInputDto.products()));
        List<ItemDto> freeItems = new ArrayList<>();
        List<PurchaseResultItemDto> purchaseItems = calculatePurchaseItems(purchaseInputDto, freeItems);

        int totalAmount = calculateItemsTotalPrice(purchaseInputDto.products());
        int promotionDiscountAmount = discountCalculator.calculatePromotionDiscountAmount(freeItems);
        int membershipDiscountAmount = discountCalculator.calculateMembershipDiscountAmount(purchaseInputDto, totalAmount);
        this.productStockService.reduceStocks(new PurchaseItemsDto(purchaseInputDto.products()));
        return createPurchaseResultDto(purchaseItems, freeItems, promotionDiscountAmount, membershipDiscountAmount,
                totalAmount);
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
        purchaseItems.add(new PurchaseResultItemDto(product.name(), product.quantity(),
                priceCalculator.calculateItemPrice(product)));
    }

    private boolean validateProduct(ItemDto product) {
        return productStockService.isExistProductWithType(product.name(), ProductType.PROMOTION)
               && productStockService.getProduct(product.name(), ProductType.PROMOTION).getType()
                  != ProductType.PROMOTION;
    }

    public int calculateItemsTotalPrice(List<ItemDto> items) {
        int totalPrice = 0;
        for (ItemDto item : items) {
            totalPrice += priceCalculator.calculateItemPrice(item);
        }
        return totalPrice;
    }


    private PurchaseResultDto createPurchaseResultDto(List<PurchaseResultItemDto> purchaseItems,
                                                      List<ItemDto> freeItems, int promotionDiscountAmount,
                                                      int membershipDiscountAmount, int totalAmount) {
        return new PurchaseResultDto.Builder().purchaseItems(purchaseItems).freeItems(freeItems)
                .promotionDiscountAmount(promotionDiscountAmount).membershipDiscountAmount(membershipDiscountAmount)
                .totalAmount(totalAmount)
                .paymentAmount(totalAmount - promotionDiscountAmount - membershipDiscountAmount).build();
    }


}
