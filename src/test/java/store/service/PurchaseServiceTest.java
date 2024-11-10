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
import store.dto.ItemDto;
import store.dto.PurchaseRequestDto;
import store.dto.PurchaseResultDto;
import store.entity.ProductStock;
import store.entity.Promotion;
import store.entity.membership.BasicMembership;
import store.entity.product.CommonProduct;
import store.entity.product.PromotionProduct;

class PurchaseServiceTest {

    @Nested
    class 상품_구매_테스트_ONLY_일반상품 {
        ProductStock productStock;
        PurchaseService purchaseService;

        static Stream<Arguments> 정상__상품_구매_테스트_케이스() {
            return Stream.of(
                    Arguments.of(
                            new PurchaseRequestDto(List.of(
                                    new ItemDto(
                                            "콜라", 2
                                    )
                            ), false),
                            new PurchaseResultDto.Builder()
                                    .purchaseItems(List.of(
                                            new ItemDto(
                                                    "콜라", 2
                                            )
                                    ))
                                    .freeItems(List.of(
                                            new ItemDto(
                                                    "콜라", 1
                                            )
                                    ))
                                    .totalAmount(2000)
                                    .paymentAmount(1000)
                                    .membershipDiscountAmount(0)
                                    .promotionDiscountAmount(1000)
                                    .build()
                    ),
                    Arguments.of(
                            new PurchaseRequestDto(List.of(
                                    new ItemDto(
                                            "밀키스", 6
                                    )
                            ), false),
                            new PurchaseResultDto.Builder()
                                    .purchaseItems(List.of(
                                            new ItemDto(
                                                    "밀키스", 6
                                            )
                                    ))
                                    .freeItems(List.of(
                                            new ItemDto(
                                                    "밀키스", 1
                                            )
                                    ))
                                    .totalAmount(6000)
                                    .promotionDiscountAmount(1000)
                                    .membershipDiscountAmount(0)
                                    .paymentAmount(5000)
                                    .build()
                    ),
                    Arguments.of(
                            new PurchaseRequestDto(List.of(
                                    new ItemDto(
                                            "카스", 3
                                    )
                            ), false),
                            new PurchaseResultDto.Builder()
                                    .purchaseItems(List.of(
                                            new ItemDto(
                                                    "카스", 3
                                            )
                                    ))
                                    .freeItems(List.of(
                                    ))
                                    .totalAmount(4000)
                                    .promotionDiscountAmount(0)
                                    .membershipDiscountAmount(0)
                                    .paymentAmount(4000)
                                    .build()
                    )
            );
        }

        @BeforeEach
        void setUp() {
            productStock = new ProductStock();
            productStock.addProduct(new CommonProduct("콜라", 1000), 5);
            productStock.addProduct(new PromotionProduct("콜라", 1000, new Promotion(
                    "프로모션", 1, 1, LocalDateTime.now(), LocalDateTime.now().plusDays(1)
            )), 5);
            productStock.addProduct(new CommonProduct("밀키스", 1000), 5);
            productStock.addProduct(new PromotionProduct("밀키스", 1000, new Promotion(
                    "프로모션2", 2, 1, LocalDateTime.now(), LocalDateTime.now().plusDays(1)
            )), 5);
            productStock.addProduct(new CommonProduct("카스", 2000), 5);
            productStock.addProduct(new PromotionProduct("카스", 1000, new Promotion(
                    "프로모션2", 2, 1, LocalDateTime.now(), LocalDateTime.now().plusDays(1)
            )), 2);
            purchaseService = new PurchaseService(
                    new ProductStockService(productStock),
                    new PromotionService(productStock),
                    new BasicMembership()
            );
        }

        @ParameterizedTest
        @MethodSource("정상__상품_구매_테스트_케이스")
        void 정상__상품_구매(PurchaseRequestDto purchaseInputDto, PurchaseResultDto expected) {
            // given

            // when
            PurchaseResultDto purchase = purchaseService.purchase(purchaseInputDto);
            System.out.println(purchase);

            // then
            Assertions.assertEquals(expected, purchase);
        }
    }

}