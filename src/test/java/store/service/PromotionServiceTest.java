package store.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import store.dto.PurchaseItemDto;
import store.dto.PurchaseItemsDto;
import store.entity.ProductStock;
import store.entity.Promotion;
import store.entity.product.CommonProduct;
import store.entity.product.PromotionProduct;

class PromotionServiceTest {

    @Nested
    class FindAdditionalFreeItems_태스트 {
        ProductStock productStock;

        static Stream<Arguments> 정상__더_받을_수_있는_증정상품_구하기_테스트_케이스() {
            return Stream.of(
                    // 구매 요청 상품이 모두 존재하고, 재고가 충분한 경우
                    Arguments.of(
                            new PurchaseItemsDto(List.of(
                                    new PurchaseItemDto("콜라", 1),
                                    new PurchaseItemDto("사이다", 1),
                                    new PurchaseItemDto("펩시", 1),
                                    new PurchaseItemDto("밀키스", 1)
                            )),
                            new PurchaseItemsDto(List.of(
                                    new PurchaseItemDto("사이다", 1),
                                    new PurchaseItemDto("펩시", 1)
                            ))
                    ),
                    Arguments.of(
                            new PurchaseItemsDto(List.of(
                                    new PurchaseItemDto("카스", 5)
                            )),
                            new PurchaseItemsDto(List.of(
                                    new PurchaseItemDto("카스", 1)
                            ))
                    ),
                    Arguments.of(
                            new PurchaseItemsDto(List.of(
                                    new PurchaseItemDto("테라", 5)
                            )),
                            new PurchaseItemsDto(List.of(
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
                    new Promotion("프로모션", 1, 1, LocalDateTime.now(), LocalDateTime.now().plusDays(1)
                    )), 3);
            productStock.addProduct(new PromotionProduct("펩시", 1000,
                    new Promotion("프로모션", 1, 1, LocalDateTime.now(), LocalDateTime.now().plusDays(1)
                    )), 3);
            productStock.addProduct(new CommonProduct("밀키스", 1000), 10);
            productStock.addProduct(new PromotionProduct("밀키스", 1000,
                    new Promotion("프로모션", 1, 1, LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(1)
                    )), 3);
            productStock.addProduct(new PromotionProduct("카스", 1000,
                    new Promotion("프로모션", 2, 1, LocalDateTime.now(), LocalDateTime.now().plusDays(1)
                    )), 6);
            productStock.addProduct(new PromotionProduct("테라", 1000,
                    new Promotion("프로모션", 2, 1, LocalDateTime.now(), LocalDateTime.now().plusDays(1)
                    )), 5);
        }

        @ParameterizedTest
        @MethodSource("정상__더_받을_수_있는_증정상품_구하기_테스트_케이스")
        void 정상__더_받을_수_있는_증정상품_구하기(PurchaseItemsDto purchaseItemsDto, PurchaseItemsDto expected) {
            // given
            PromotionService promotionService = new PromotionService(productStock);

            // when
            PurchaseItemsDto result = promotionService.findAdditionalFreeItems(purchaseItemsDto);

            // then
            for (int i = 0; i < expected.products().size(); i++) {
                Assertions.assertEquals(expected.products().get(i).name(), result.products().get(i).name());
                Assertions.assertEquals(expected.products().get(i).quantity(), result.products().get(i).quantity());
            }
        }


    }


    @Nested
    class calculateFreeCount_테스트 {
        ProductStock productStock;

        static Stream<Arguments> 정상__증정상품_수량_계산하기() {
            return Stream.of(
                    Arguments.of(new PurchaseItemDto("콜라", 1), 0),
                    Arguments.of(new PurchaseItemDto("사이다", 2), 1),
                    Arguments.of(new PurchaseItemDto("펩시", 1), 0),
                    Arguments.of(new PurchaseItemDto("밀키스", 2), 0),
                    Arguments.of(new PurchaseItemDto("카스", 6), 2),
                    Arguments.of(new PurchaseItemDto("테라", 3), 1)
            );
        }

        @BeforeEach
        void setup() {
            productStock = new ProductStock();
            productStock.addProduct(new CommonProduct("콜라", 1000), 10);
            productStock.addProduct(new CommonProduct("사이다", 1000), 10);
            productStock.addProduct(new PromotionProduct("사이다", 1000,
                    new Promotion("프로모션", 1, 1, LocalDateTime.now(), LocalDateTime.now().plusDays(1)
                    )), 3);
            productStock.addProduct(new PromotionProduct("펩시", 1000,
                    new Promotion("프로모션", 1, 1, LocalDateTime.now(), LocalDateTime.now().plusDays(1)
                    )), 3);
            productStock.addProduct(new CommonProduct("밀키스", 1000), 10);
            productStock.addProduct(new PromotionProduct("밀키스", 1000,
                    new Promotion("프로모션", 1, 1, LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(1)
                    )), 3);
            productStock.addProduct(new PromotionProduct("카스", 1000,
                    new Promotion("프로모션", 2, 1, LocalDateTime.now(), LocalDateTime.now().plusDays(1)
                    )), 6);
            productStock.addProduct(new PromotionProduct("테라", 1000,
                    new Promotion("프로모션", 2, 1, LocalDateTime.now(), LocalDateTime.now().plusDays(1)
                    )), 5);
        }

        @ParameterizedTest
        @MethodSource()
        void 정상__증정상품_수량_계산하기(PurchaseItemDto purchaseItemDto, int expected) {
            // given
            PromotionService promotionService = new PromotionService(productStock);

            // when
            int result = promotionService.calculateFreeCount(purchaseItemDto);

            // then
            Assertions.assertEquals(expected, result);
        }
    }
}