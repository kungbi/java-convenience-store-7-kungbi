package store.entity.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.stream.Stream;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import store.entity.Promotion;
import store.exception.ProductException;
import store.exception.message.ProductExceptionMessage;

class PromotionProductTest {

    @Nested
    class 객체_생성_테스트 {
        @Test
        void 정상__객체_생성() {
            // given
            Promotion promotion = new Promotion("프로모션1", 2, 1,
                    LocalDateTime.now(), LocalDateTime.now().plusDays(7));
            PromotionProduct promotionProduct = new PromotionProduct("콜라", 1000, 10, promotion);

            // when
            ProductType type = promotionProduct.getType();

            // then
            assertEquals(ProductType.PROMOTION, type);
        }
    }


    @Nested
    class 금액_계산_테스트 {

        static Stream<Arguments> 정상__금액_계산_테스트케이스() {
            return Stream.of(
                    Arguments.of(
                            new PromotionProduct("콜라", 1000, 10,
                                    new Promotion("프로모션1", 2, 1,
                                            LocalDateTime.now(), LocalDateTime.now().plusDays(7))
                            ),
                            3,
                            2000
                    ),
                    Arguments.of(
                            new PromotionProduct("콜라", 1000, 10,
                                    new Promotion("프로모션1", 2, 1,
                                            LocalDateTime.now(), LocalDateTime.now().plusDays(7))
                            ),
                            1,
                            1000
                    ),
                    Arguments.of(
                            new PromotionProduct("콜라", 1000, 10,
                                    new Promotion("프로모션1", 2, 1,
                                            LocalDateTime.now(), LocalDateTime.now().plusDays(7))
                            ),
                            6,
                            4000
                    ),
                    Arguments.of(
                            new PromotionProduct("콜라", 1000, 10,
                                    new Promotion("프로모션1", 2, 1,
                                            LocalDateTime.now(), LocalDateTime.now().plusDays(7))
                            ),
                            4,
                            3000
                    ),
                    Arguments.of(
                            new PromotionProduct("콜라", 1000, 10,
                                    new Promotion("프로모션1", 1, 1,
                                            LocalDateTime.now(), LocalDateTime.now().plusDays(7))
                            ),
                            4,
                            2000
                    )
            );
        }

        @ParameterizedTest
        @MethodSource("정상__금액_계산_테스트케이스")
        void 정상__금액_계산(PromotionProduct promotionProduct, int quantity, int expected) {
            // given
            // when
            int price = promotionProduct.calculatePrice(quantity);

            // then
            assertEquals(expected, price);
        }

        @Test
        void 예외__프로모션_종료() {
            // given
            Promotion promotion = new Promotion("프로모션1", 2, 1,
                    LocalDateTime.now().minusDays(7), LocalDateTime.now().minusDays(1));
            PromotionProduct promotionProduct = new PromotionProduct("콜라", 1000, 10, promotion);

            // when
            int price = promotionProduct.calculatePrice(3);

            // then
            assertEquals(3000, price);
        }

        @Test
        void 예외__구매수량_음수() {
            // given
            Promotion promotion = new Promotion("프로모션1", 2, 1,
                    LocalDateTime.now().minusDays(7), LocalDateTime.now().minusDays(1));
            PromotionProduct promotionProduct = new PromotionProduct("콜라", 1000, 10, promotion);

            // when
            ProductException exception = assertThrows(ProductException.class,
                    () -> promotionProduct.calculatePrice(-1));

            // then
            assertEquals(ProductExceptionMessage.NEGATIVE_QUANTITY.getMessage(), exception.getMessage());
        }
    }

}