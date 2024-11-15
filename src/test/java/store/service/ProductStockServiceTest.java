package store.service;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import store.dto.ItemDto;
import store.dto.PurchaseItemsDto;
import store.entity.ProductStock;
import store.entity.Promotion;
import store.entity.product.CommonProduct;
import store.entity.product.ProductType;
import store.entity.product.PromotionProduct;
import store.exception.ProductStockException;
import store.exception.message.ExceptionMessage;
import store.exception.message.ProductStockExceptionMessage;
import store.service.ProductStockService.ProductStockService;
import store.utils.Date.LocalDateTimes;

class ProductStockServiceTest {

    @Nested
    class 상품_구매가_가능한지_검증하는_기능_테스트 {
        ProductStock productStock;

        static Stream<Arguments> 정상__상품_구매_가능_테스트_케이스() {
            return Stream.of(
                    // 구매 요청 상품이 모두 존재하고, 재고가 충분한 경우
                    Arguments.of(
                            new PurchaseItemsDto(
                                    List.of(
                                            new ItemDto("콜라", 1),
                                            new ItemDto("사이다", 1)
                                    )
                            ),
                            true
                    ),
                    // 적용 가능한 프로모션 상품의 재고가 부족하지만, 일반 상품의 재고는 충분한 경우
                    Arguments.of(
                            new PurchaseItemsDto(
                                    List.of(
                                            new ItemDto("사이다", 4)
                                    )
                            ),
                            true
                    ),
                    // 프로모션 상품 적용이 불가능한데(기간), 일반 상품의 재고가 충분한 경우
                    Arguments.of(
                            new PurchaseItemsDto(
                                    List.of(
                                            new ItemDto("밀키스", 3)
                                    )
                            ),
                            true
                    )
            );
        }

        static Stream<Arguments> 예외__상품_구매_가능_테스트_케이스() {
            return Stream.of(
                    //  프로모션 상품 적용이 가능하고, 프로모션 상품의 재고가 부족한 경우
                    Arguments.of(
                            new PurchaseItemsDto(
                                    List.of(
                                            new ItemDto("펩시", 4)
                                    )
                            ),
                            ProductStockExceptionMessage.INSUFFICIENT_STOCK
                    ),
                    // 구매 요청 상품 중 하나라도 존재하지 않는 경우
                    Arguments.of(
                            new PurchaseItemsDto(
                                    List.of(
                                            new ItemDto("콜라", 1),
                                            new ItemDto("없는상품", 1)
                                    )
                            ),
                            ProductStockExceptionMessage.NOT_EXIST_PRODUCT
                    ),
                    // 일반 상품의 경우, 재고가 부족한 경우
                    Arguments.of(
                            new PurchaseItemsDto(
                                    List.of(
                                            new ItemDto("콜라", 100)
                                    )
                            ),
                            ProductStockExceptionMessage.INSUFFICIENT_STOCK
                    ),
                    // 프로모션 상품 적용이 불가능한데(기간), 일반 상품의 재고가 부족한 경우
                    Arguments.of(
                            new PurchaseItemsDto(
                                    List.of(
                                            new ItemDto("밀키스", 11)
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
                    new Promotion("프로모션", 1, 1, LocalDateTimes.now(), LocalDateTimes.now().plusDays(1)
                    )), 3);
            productStock.addProduct(new CommonProduct("펩시", 1000), 0);
            productStock.addProduct(new PromotionProduct("펩시", 1000,
                    new Promotion("프로모션", 1, 1, LocalDateTimes.now(), LocalDateTimes.now().plusDays(1)
                    )), 3);
            productStock.addProduct(new CommonProduct("밀키스", 1000), 10);
            productStock.addProduct(new PromotionProduct("밀키스", 1000,
                    new Promotion("프로모션", 1, 1, LocalDateTimes.now().minusDays(2), LocalDateTimes.now().minusDays(1)
                    )), 3);
        }

        @ParameterizedTest
        @MethodSource("정상__상품_구매_가능_테스트_케이스")
        void 정상__상품_구매_가능(PurchaseItemsDto purchaseRequestDto, boolean expected) {
            // given
            ProductStockService productStockService = new ProductStockService(productStock);

            // when & then
            productStockService.validateStocks(purchaseRequestDto); // 예외가 발생하지 않으면 성공
        }

        @ParameterizedTest
        @MethodSource("예외__상품_구매_가능_테스트_케이스")
        void 예외__상품_구매_가능_테스트(PurchaseItemsDto purchaseItemsDto, ExceptionMessage message) {
            // given
            ProductStockService productStockService = new ProductStockService(productStock);

            // when
            ProductStockException exception = Assertions.assertThrows(ProductStockException.class,
                    () -> productStockService.validateStocks(purchaseItemsDto));

            // then
            Assertions.assertEquals(message.getMessage(), exception.getMessage());
        }
    }

    @Nested
    class 상품_재고_수정_기능_테스트 {
        ProductStock productStock;

        static Stream<Arguments> 정상__상품_재고_수정_테스트_케이스() {
            return Stream.of(
                    Arguments.of(
                            new PurchaseItemsDto(
                                    List.of(
                                            new ItemDto("콜라", 1),
                                            new ItemDto("사이다", 1)
                                    )
                            ),
                            List.of(
                                    new StockExpected("콜라", 9, ProductType.COMMON),
                                    new StockExpected("사이다", 2, ProductType.PROMOTION),
                                    new StockExpected("사이다", 10, ProductType.COMMON)
                            )
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
                    new Promotion("프로모션", 1, 1, LocalDateTimes.now(), LocalDateTimes.now().plusDays(1)
                    )), 3);
            productStock.addProduct(new CommonProduct("밀키스", 1000), 10);
            productStock.addProduct(new PromotionProduct("밀키스", 1000,
                    new Promotion("프로모션", 1, 1, LocalDateTimes.now().minusDays(2), LocalDateTimes.now().minusDays(1)
                    )), 3);
        }

        @ParameterizedTest
        @MethodSource("정상__상품_재고_수정_테스트_케이스")
        void 정상__상품_재고_수정(PurchaseItemsDto purchaseItemsDto, List<StockExpected> expected) {
            // given
            ProductStockService productStockService = new ProductStockService(productStock);

            // when
            productStockService.reduceStocks(purchaseItemsDto);

            // then
            for (StockExpected stockExpected : expected) {
                int quantity = productStock.getProductQuantity(stockExpected.name(), stockExpected.type);
                Assertions.assertEquals(stockExpected.quantity(), quantity);
            }
        }

        static class StockExpected {
            private final String name;
            private final int quantity;
            private final ProductType type;

            public StockExpected(String name, int quantity, ProductType type) {
                this.name = name;
                this.quantity = quantity;
                this.type = type;
            }

            public String name() {
                return name;
            }

            public int quantity() {
                return quantity;
            }

            public ProductType getType() {
                return type;
            }
        }
    }
}