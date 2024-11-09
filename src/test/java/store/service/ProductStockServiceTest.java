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
import store.exception.ProductStockException;
import store.exception.message.ExceptionMessage;
import store.exception.message.ProductStockExceptionMessage;

class ProductStockServiceTest {

    @Nested
    class isAvailableToPurchase_테스트 {
        ProductStock productStock;

        static Stream<Arguments> 정상__상품_구매_가능_테스트_케이스() {
            return Stream.of(
                    // 구매 요청 상품이 모두 존재하고, 재고가 충분한 경우
                    Arguments.of(
                            new PurchaseItemsDto(
                                    List.of(
                                            new PurchaseItemDto("콜라", 1),
                                            new PurchaseItemDto("사이다", 1)
                                    )
                            ),
                            true
                    ),
                    // 적용 가능한 프로모션 상품의 재고가 부족하지만, 일반 상품의 재고는 충분한 경우
                    Arguments.of(
                            new PurchaseItemsDto(
                                    List.of(
                                            new PurchaseItemDto("사이다", 4)
                                    )
                            ),
                            true
                    ),
                    // 프로모션 상품 적용이 불가능한데(기간), 일반 상품의 재고가 충분한 경우
                    Arguments.of(
                            new PurchaseItemsDto(
                                    List.of(
                                            new PurchaseItemDto("밀키스", 3)
                                    )
                            ),
                            true
                    )
            );
        }

        static Stream<Arguments> 예외__상품_구매_가능_테스트_케이스() {
            return Stream.of(
                    //  프로모션 상품 적용이 가능하고, 프로모션 상품만 존재하며, 프로모션 상품의 재고가 부족한 경우
                    Arguments.of(
                            new PurchaseItemsDto(
                                    List.of(
                                            new PurchaseItemDto("펩시", 4)
                                    )
                            ),
                            ProductStockExceptionMessage.INSUFFICIENT_STOCK
                    ),
                    // 구매 요청 상품 중 하나라도 존재하지 않는 경우
                    Arguments.of(
                            new PurchaseItemsDto(
                                    List.of(
                                            new PurchaseItemDto("콜라", 1),
                                            new PurchaseItemDto("없는상품", 1)
                                    )
                            ),
                            ProductStockExceptionMessage.NOT_EXIST_PRODUCT
                    ),
                    // 일반 상품의 경우, 재고가 부족한 경우
                    Arguments.of(
                            new PurchaseItemsDto(
                                    List.of(
                                            new PurchaseItemDto("콜라", 100)
                                    )
                            ),
                            ProductStockExceptionMessage.INSUFFICIENT_STOCK
                    ),
                    // 프로모션 상품 적용이 불가능한데(기간), 일반 상품의 재고가 부족한 경우
                    Arguments.of(
                            new PurchaseItemsDto(
                                    List.of(
                                            new PurchaseItemDto("밀키스", 11)
                                    )
                            ),
                            ProductStockExceptionMessage.INSUFFICIENT_STOCK
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
        }

        @ParameterizedTest
        @MethodSource("정상__상품_구매_가능_테스트_케이스")
        void 정상__상품_구매_가능(PurchaseItemsDto purchaseRequestDto, boolean expected) {
            // given
            ProductStockService productStockService = new ProductStockService(productStock);

            // when
            boolean result = productStockService.validateProductsAvailability(purchaseRequestDto);

            // then
            Assertions.assertEquals(expected, result);
        }

        @ParameterizedTest
        @MethodSource("예외__상품_구매_가능_테스트_케이스")
        void 예외__상품_구매_가능_테스트(PurchaseItemsDto purchaseItemsDto, ExceptionMessage message) {
            // given
            ProductStockService productStockService = new ProductStockService(productStock);

            // when
            ProductStockException exception = Assertions.assertThrows(ProductStockException.class,
                    () -> productStockService.validateProductsAvailability(purchaseItemsDto));

            // then
            Assertions.assertEquals(message.getMessage(), exception.getMessage());
        }
    }
}