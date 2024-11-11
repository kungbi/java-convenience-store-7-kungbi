package store.service;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import store.dto.AdditionalFreeItemsDto;
import store.dto.ExcludedPromotionItemsDto;
import store.dto.ItemDto;
import store.dto.PurchaseItemsDto;
import store.entity.ProductStock;
import store.entity.Promotion;
import store.entity.product.CommonProduct;
import store.entity.product.PromotionProduct;
import store.service.PromotionService.PromotionService;
import store.utils.Date.LocalDateTimes;

class PromotionServiceTest {

    @Nested
    class 기능_findAdditionalFreeItems_태스트 {
        ProductStock productStock;

        static Stream<Arguments> 정상__더_받을_수_있는_증정상품_구하기_테스트_케이스() {
            return Stream.of(
                    // 구매 요청 상품이 모두 존재하고, 재고가 충분한 경우
                    Arguments.of(
                            new PurchaseItemsDto(List.of(
                                    new ItemDto("콜라", 1),
                                    new ItemDto("사이다", 1),
                                    new ItemDto("펩시", 1),
                                    new ItemDto("밀키스", 1)
                            )),
                            new AdditionalFreeItemsDto(List.of(
                                    new ItemDto("사이다", 1),
                                    new ItemDto("펩시", 1)
                            ))
                    ),
                    Arguments.of(
                            new PurchaseItemsDto(List.of(
                                    new ItemDto("카스", 5)
                            )),
                            new AdditionalFreeItemsDto(List.of(
                                    new ItemDto("카스", 1)
                            ))
                    ),
                    Arguments.of(
                            new PurchaseItemsDto(List.of(
                                    new ItemDto("테라", 5)
                            )),
                            new AdditionalFreeItemsDto(List.of(
                            ))
                    ),
                    Arguments.of(
                            new PurchaseItemsDto(List.of(
                                    new ItemDto("과자", 8)
                            )),
                            new AdditionalFreeItemsDto(List.of(
                            ))
                    ),
                    Arguments.of(
                            new PurchaseItemsDto(List.of(
                                    new ItemDto("바나나", 10)
                            )),
                            new AdditionalFreeItemsDto(List.of(
                            ))
                    )
            );
        }

        @BeforeEach
        void setUp() {
            productStock = new ProductStock();
            productStock.addProduct(new CommonProduct("콜라", 1000), 10);
            productStock.addProduct(new CommonProduct("사이다", 1000), 10);
            productStock.addProduct(new PromotionProduct("사이다", 1000,
                    new Promotion("프로모션", 1, 1, LocalDateTimes.now(), LocalDateTimes.now().plusDays(1)
                    )), 3);
            productStock.addProduct(new PromotionProduct("펩시", 1000,
                    new Promotion("프로모션2", 1, 1, LocalDateTimes.now(), LocalDateTimes.now().plusDays(1)
                    )), 3);
            productStock.addProduct(new CommonProduct("밀키스", 1000), 10);
            productStock.addProduct(new PromotionProduct("밀키스", 1000,
                    new Promotion("프로모션3", 1, 1, LocalDateTimes.now().minusDays(2), LocalDateTimes.now().minusDays(1)
                    )), 3);
            productStock.addProduct(new PromotionProduct("카스", 1000,
                    new Promotion("프로모션4", 2, 1, LocalDateTimes.now(), LocalDateTimes.now().plusDays(1)
                    )), 6);
            productStock.addProduct(new PromotionProduct("테라", 1000,
                    new Promotion("프로모션5", 2, 1, LocalDateTimes.now(), LocalDateTimes.now().plusDays(1)
                    )), 5);
            productStock.addProduct(new PromotionProduct("과자", 1000,
                    new Promotion("프로모션6", 2, 1, LocalDateTimes.now(), LocalDateTimes.now().plusDays(1)
                    )), 8);
            productStock.addProduct(new PromotionProduct("바나나", 1000,
                    new Promotion("프로모션7", 1, 1, LocalDateTimes.now(), LocalDateTimes.now().plusDays(1)
                    )), 10);
        }

        @ParameterizedTest
        @MethodSource("정상__더_받을_수_있는_증정상품_구하기_테스트_케이스")
        void 정상__더_받을_수_있는_증정상품_구하기(PurchaseItemsDto purchaseItemsDto, AdditionalFreeItemsDto expected) {
            // given
            PromotionService promotionService = new PromotionService(productStock);

            // when
            AdditionalFreeItemsDto result = promotionService.findAdditionalFreeItems(purchaseItemsDto);

            // then
            for (int i = 0; i < expected.products().size(); i++) {
                Assertions.assertEquals(expected.products().get(i).name(), result.products().get(i).name());
                Assertions.assertEquals(expected.products().get(i).quantity(), result.products().get(i).quantity());
            }
        }


    }

    @Nested
    class 기능_calculateFreeCount_테스트 {
        ProductStock productStock;

        static Stream<Arguments> 정상__증정상품_수량_계산하기() {
            return Stream.of(
                    Arguments.of(new ItemDto("콜라", 1), 0),
                    Arguments.of(new ItemDto("사이다", 2), 1),
                    Arguments.of(new ItemDto("펩시", 1), 0),
                    Arguments.of(new ItemDto("밀키스", 2), 0),
                    Arguments.of(new ItemDto("카스", 6), 2),
                    Arguments.of(new ItemDto("테라", 3), 1),
                    Arguments.of(new ItemDto("바나나", 5), 0)
            );
        }

        @BeforeEach
        void setup() {
            productStock = new ProductStock();
            productStock.addProduct(new CommonProduct("콜라", 1000), 10);
            productStock.addProduct(new CommonProduct("사이다", 1000), 10);
            productStock.addProduct(new PromotionProduct("사이다", 1000,
                    new Promotion("프로모션", 1, 1, LocalDateTimes.now(), LocalDateTimes.now().plusDays(1)
                    )), 3);
            productStock.addProduct(new PromotionProduct("펩시", 1000,
                    new Promotion("프로모션", 1, 1, LocalDateTimes.now(), LocalDateTimes.now().plusDays(1)
                    )), 3);
            productStock.addProduct(new CommonProduct("밀키스", 1000), 10);
            productStock.addProduct(new PromotionProduct("밀키스", 1000,
                    new Promotion("프로모션", 1, 1, LocalDateTimes.now().minusDays(2), LocalDateTimes.now().minusDays(1)
                    )), 3);
            productStock.addProduct(new PromotionProduct("카스", 1000,
                    new Promotion("프로모션", 2, 1, LocalDateTimes.now(), LocalDateTimes.now().plusDays(1)
                    )), 6);
            productStock.addProduct(new PromotionProduct("테라", 1000,
                    new Promotion("프로모션", 2, 1, LocalDateTimes.now(), LocalDateTimes.now().plusDays(1)
                    )), 5);
            productStock.addProduct(new CommonProduct("바나나", 1000), 10);
            productStock.addProduct(new PromotionProduct("바나나", 1000,
                    new Promotion("프로모션", 2, 1, LocalDateTimes.now(), LocalDateTimes.now().plusDays(1)
                    )), 0);
        }

        @ParameterizedTest
        @MethodSource()
        void 정상__증정상품_수량_계산하기(ItemDto purchaseItemDto, int expected) {
            // given
            PromotionService promotionService = new PromotionService(productStock);

            // when
            int result = promotionService.calculateFreeCount(purchaseItemDto);

            // then
            Assertions.assertEquals(expected, result);
        }
    }

    @Nested
    class 기능_findExcludedPromotionItems_테스트 {
        ProductStock productStock;

        static Stream<Arguments> 정상__증정상품_수량_계산하기() {
            return Stream.of(
                    Arguments.of(
                            new PurchaseItemsDto(List.of(new ItemDto("사이다", 3))),
                            new ExcludedPromotionItemsDto(List.of(new ItemDto("사이다", 1)))
                    ),
                    Arguments.of(
                            new PurchaseItemsDto(List.of(new ItemDto("테라", 5))),
                            new ExcludedPromotionItemsDto(List.of(new ItemDto("테라", 2)))
                    )
            );
        }

        @BeforeEach
        void setup() {
            productStock = new ProductStock();
            productStock.addProduct(new CommonProduct("콜라", 1000), 10);
            productStock.addProduct(new CommonProduct("사이다", 1000), 10);
            productStock.addProduct(new PromotionProduct("사이다", 1000,
                    new Promotion("프로모션", 1, 1, LocalDateTimes.now(), LocalDateTimes.now().plusDays(1)
                    )), 3);
            productStock.addProduct(new PromotionProduct("펩시", 1000,
                    new Promotion("프로모션", 1, 1, LocalDateTimes.now(), LocalDateTimes.now().plusDays(1)
                    )), 3);
            productStock.addProduct(new CommonProduct("밀키스", 1000), 10);
            productStock.addProduct(new PromotionProduct("밀키스", 1000,
                    new Promotion("프로모션", 1, 1, LocalDateTimes.now().minusDays(2), LocalDateTimes.now().minusDays(1)
                    )), 3);
            productStock.addProduct(new PromotionProduct("카스", 1000,
                    new Promotion("프로모션", 2, 1, LocalDateTimes.now(), LocalDateTimes.now().plusDays(1)
                    )), 6);
            productStock.addProduct(new PromotionProduct("테라", 1000,
                    new Promotion("프로모션", 2, 1, LocalDateTimes.now(), LocalDateTimes.now().plusDays(1)
                    )), 5);
        }

        @ParameterizedTest
        @MethodSource("정상__증정상품_수량_계산하기")
        void 정상__프로모션_제외_상품_구하기(PurchaseItemsDto purchaseItemsDto, ExcludedPromotionItemsDto expected) {
            // given
            PromotionService promotionService = new PromotionService(productStock);

            // when
            ExcludedPromotionItemsDto result = promotionService.findExcludedPromotionItems(purchaseItemsDto);

            // then
            for (int i = 0; i < expected.items().size(); i++) {
                Assertions.assertEquals(expected.items().get(i).name(), result.items().get(i).name());
                Assertions.assertEquals(expected.items().get(i).quantity(), result.items().get(i).quantity());
            }
        }
    }
}