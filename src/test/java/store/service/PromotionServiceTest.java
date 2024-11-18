package store.service;

import camp.nextstep.edu.missionutils.DateTimes;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import store.config.ProductType;
import store.domain.Promotion;
import store.domain.Stock;
import store.dto.GetExtraFreeProductsDto.GetExtraFreeProductsInputDto;
import store.dto.GetExtraFreeProductsDto.GetExtraFreeProductsOutputDto;
import store.dto.ItemDto;
import store.repository.ProductPromotionRepository;
import store.repository.ProductRepository;
import store.repository.PromotionRepository;

class PromotionServiceTest {
    PromotionService promotionService;

    static Stream<Arguments> 추가_증정_상품_계산_테스트_케이스() {
        return Stream.of(
                Arguments.of(
                        List.of(
                                new ItemDto("콜라", 2)
                        ),
                        List.of(
                                new ItemDto("콜라", 1)
                        )
                ),
                Arguments.of(
                        List.of(
                                new ItemDto("콜라", 1)
                        ),
                        List.of(
                        )
                ),
                Arguments.of(
                        List.of(
                                new ItemDto("콜라", 5)
                        ),
                        List.of(
                                new ItemDto("콜라", 1)
                        )
                ),
                Arguments.of(
                        List.of(
                                new ItemDto("콜라", 8)
                        ),
                        List.of(
                        )
                ),
                Arguments.of(
                        List.of(
                                new ItemDto("콜라", 2),
                                new ItemDto("콜라", 2)
                        ),
                        List.of(
                        )
                )
        );
    }

    @BeforeEach
    void setUp() {
        PromotionRepository promotionRepository = new PromotionRepository();
        ProductRepository productRepository = new ProductRepository();
        ProductPromotionRepository productPromotionRepository = new ProductPromotionRepository();
        Stock stock = new Stock();

        productRepository.add("콜라", 1000, ProductType.COMMON);
        productRepository.add("콜라", 1000, ProductType.PROMOTION);
        stock.add(0, 5);
        stock.add(1, 8);

        Promotion promotion = new Promotion("행사", 2, 1, DateTimes.now(), DateTimes.now());
        promotionRepository.add(promotion);
        productPromotionRepository.add("콜라", promotion);

        promotionService = new PromotionService(productRepository, promotionRepository, productPromotionRepository,
                stock);
    }

    @ParameterizedTest
    @MethodSource("추가_증정_상품_계산_테스트_케이스")
    void 추가_증정_상품_계산(List<ItemDto> items, List<ItemDto> expected) {
        // given

        // when
        GetExtraFreeProductsOutputDto extraFreeProductsOutputDto = promotionService.getExtraFreeProductsOutputDto(
                new GetExtraFreeProductsInputDto(items));

        // then
        Assertions.assertEquals(expected, extraFreeProductsOutputDto.additionalProducts());
    }

}