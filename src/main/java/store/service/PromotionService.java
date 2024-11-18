package store.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import store.config.ProductType;
import store.domain.Product;
import store.domain.Promotion;
import store.domain.Stock;
import store.dto.GetExceptPromotionProductsDto.GetExceptPromotionProductsInputDto;
import store.dto.GetExceptPromotionProductsDto.GetExceptPromotionProductsOutputDto;
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
        List<ItemDto> groupedItems = groupingItem(input.purchaseItems());

        List<ItemDto> additionalProducts = new ArrayList<>();
        for (ItemDto item : groupedItems) {
            Optional<ItemDto> additionalFreeProduct = getExtraFreeProduct(item);
            additionalFreeProduct.ifPresent(additionalProducts::add);
        }

        return new GetExtraFreeProductsOutputDto(additionalProducts);
    }

    public GetExceptPromotionProductsOutputDto getExceptPromotionProductsOutputDto(
            GetExceptPromotionProductsInputDto inputDto) {
        List<ItemDto> items = groupingItem(inputDto.purchaseItems());

        List<ItemDto> exceptedProducts = new ArrayList<>();
        for (ItemDto item : items) {
            Optional<ItemDto> exceptPromotionProduct = getExceptPromotionProduct(item);
            exceptPromotionProduct.ifPresent(exceptedProducts::add);
        }
        return new GetExceptPromotionProductsOutputDto(exceptedProducts);
    }

    private Optional<ItemDto> getExceptPromotionProduct(ItemDto itemDto) {
        // 프로모션 상품이 있는지 확인
        Optional<Product> product = productRepository.findByNameAndType(itemDto.productName(), ProductType.PROMOTION);
        if (product.isEmpty()) {
            return Optional.empty(); // 프로모션이 적용되지 않은 상품
        }

        Optional<Promotion> promotion = productPromotionRepository.findByProductName(itemDto.productName());
        if (promotion.isEmpty()) {
            return Optional.empty(); // 프로모션 2차 확인
        }

        if (promotion.get().isDateAvailable()) { // 적용 가능한 기간이 아님. 때문에 모든 수량 정가
            return Optional.of(new ItemDto(itemDto.productName(), itemDto.quantity()));
        }

        int exceptedQuantity = getExceptedQuantity(itemDto, product.get(), promotion.get());
        if (exceptedQuantity != 0) {
            return Optional.of(new ItemDto(itemDto.productName(), exceptedQuantity));
        }
        return Optional.empty();
    }

    private int getExceptedQuantity(ItemDto itemDto, Product product, Promotion promotion) {
        int promotionProductQuantity = stock.getQuantity(product.getId());
        int promotionAppliedQuantity = promotion
                .getPromotionAppliedQuantity(Math.min(promotionProductQuantity, itemDto.quantity()));
        return itemDto.quantity() - promotionAppliedQuantity;
    }

    private Optional<ItemDto> getExtraFreeProduct(ItemDto item) {
        Optional<Product> product = productRepository.findByNameAndType(item.productName(), ProductType.PROMOTION);
        if (product.isEmpty()) {
            return Optional.empty();
        }

        Optional<Promotion> promotion = productPromotionRepository.findByProductName(product.get().getName());
        if (promotion.isEmpty() || promotion.get().isDateAvailable()) {
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

    private List<ItemDto> groupingItem(List<ItemDto> purchaseItems) {
        return purchaseItems.stream()
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
