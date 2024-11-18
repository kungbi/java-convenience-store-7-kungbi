package store.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import store.config.ProductType;
import store.domain.Product;
import store.domain.Promotion;
import store.domain.Stock;
import store.dto.GetExtraFreeProductsDto.GetExtraFreeProductsInputDto;
import store.dto.GetExtraFreeProductsDto.GetExtraFreeProductsOutputDto;
import store.dto.ItemDto;
import store.repository.ProductPromotionRepository;
import store.repository.ProductRepository;
import store.repository.PromotionRepository;

public class PromotionService {
    private final ProductRepository productRepository;
    private final PromotionRepository promotionRepository;
    private final ProductPromotionRepository productPromotionRepository;
    private final Stock stock;

    public PromotionService(ProductRepository productRepository, PromotionRepository promotionRepository,
                            ProductPromotionRepository productPromotionRepository, Stock stock) {
        this.productRepository = productRepository;
        this.promotionRepository = promotionRepository;
        this.productPromotionRepository = productPromotionRepository;
        this.stock = stock;
    }

    public GetExtraFreeProductsOutputDto getExtraFreeProductsOutputDto(
            GetExtraFreeProductsInputDto input) {
        List<ItemDto> groupedItems = groupingItem(input);

        List<ItemDto> additionalProducts = new ArrayList<>();
        for (ItemDto item : groupedItems) {
            Optional<ItemDto> additionalFreeProduct = getExtraFreeProduct(item);
            additionalFreeProduct.ifPresent(additionalProducts::add);
        }

        return new GetExtraFreeProductsOutputDto(additionalProducts);
    }

    private Optional<ItemDto> getExtraFreeProduct(ItemDto item) {
        Optional<Product> product = productRepository.findByNameAndType(item.productName(), ProductType.PROMOTION);
        if (product.isEmpty()) {
            return Optional.empty();
        }

        Optional<Promotion> promotion = productPromotionRepository.findByProductName(product.get().getName());
        if (promotion.isEmpty() || !promotion.get().isDateAvailable()) {
            return Optional.empty();
        }

        if (!promotion.get().hasRequiredPurchaseQuantity(item.quantity())) {
            return Optional.empty();
        }

        if (!stock.isSufficient(product.get().getId(), item.quantity() + promotion.get().getGet())) {
            return Optional.empty();
        }

        return Optional.of(new ItemDto(item.productName(), promotion.get().getGet()));
    }

    private List<ItemDto> groupingItem(GetExtraFreeProductsInputDto input) {
        return input.purchaseItems().stream()
                .collect(Collectors.groupingBy(
                        ItemDto::productName, // 그룹화 기준: productName
                        Collectors.summingInt(ItemDto::quantity) // 수량 합산
                ))
                .entrySet()
                .stream()
                .map(entry -> new ItemDto(entry.getKey(), entry.getValue())) // Map을 ItemDto로 변환
                .collect(Collectors.toList());
    }

//    프로모션인지 확인
//    추가 가능한지 확인
//    재고가 충분한지 확인
//            반환

//    할인 제외
//    프로모션이 상품 재고가 남아있는지
//    할인되는 상품이 몇개인지 계산
//    사용자가 구입하려면 개수의 차 반환
}
