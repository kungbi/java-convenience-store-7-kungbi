package store.service.PurchaseService;

import java.util.List;
import store.dto.ItemDto;
import store.dto.PurchaseRequestDto;
import store.entity.membership.Membership;
import store.entity.product.ProductType;
import store.service.ProductStockService;

public class DiscountCalculator {
    private final Membership membership;
    private final ProductStockService productStockService;

    public DiscountCalculator(Membership membership, ProductStockService productStockService) {
        this.membership = membership;
        this.productStockService = productStockService;
    }


    public int calculateMembershipDiscountAmount(PurchaseRequestDto purchaseInputDto, int totalAmount) {
        int membershipDiscountAmount = 0;
        if (purchaseInputDto.isMembership()) {
            membershipDiscountAmount = membership.applyDiscount(totalAmount);
        }
        return membershipDiscountAmount;
    }

    public Integer calculatePromotionDiscountAmount(List<ItemDto> freeItems) {
        return freeItems.stream().reduce(0, (sum, item) -> sum + productStockService.getProduct(item.name(),
                ProductType.PROMOTION).getPrice() * item.quantity(), Integer::sum);
    }


}
