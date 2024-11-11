package store.entity.product;

import java.time.LocalDateTime;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import store.entity.Promotion;
import store.utils.Date.LocalDateTimes;

class PromotionProductTest {

    static Stream<Arguments> 정상__프로모션_상태_확인_테스트_케이스() {
        return Stream.of(
                Arguments.of(LocalDateTimes.now(), LocalDateTimes.now().plusDays(7), true),
                Arguments.of(LocalDateTimes.now().minusDays(1), LocalDateTimes.now().plusDays(7), true),
                Arguments.of(LocalDateTimes.now().minusDays(1), LocalDateTimes.now().minusDays(1), false),
                Arguments.of(LocalDateTimes.now().plusDays(1), LocalDateTimes.now().plusDays(7), false)
        );
    }

    @Test
    void 정상__프로모션_확인() {
        // given
        Promotion promotion = new Promotion("프로모션1", 2, 1,
                LocalDateTimes.now(), LocalDateTimes.now().plusDays(7));
        PromotionProduct promotionProduct = new PromotionProduct("콜라", 1000, promotion);

        // when
        Promotion promotionFromProduct = promotionProduct.getPromotion();

        // then
        Assertions.assertEquals(promotion, promotionFromProduct);
    }

    @Test
    void 정상__타입_확인() {
        // given
        Promotion promotion = new Promotion("프로모션1", 2, 1,
                LocalDateTimes.now(), LocalDateTimes.now().plusDays(7));
        PromotionProduct promotionProduct = new PromotionProduct("콜라", 1000, promotion);

        // when
        ProductType type = promotionProduct.getType();

        // then
        Assertions.assertEquals(ProductType.PROMOTION, type);
    }

    @ParameterizedTest
    @MethodSource("정상__프로모션_상태_확인_테스트_케이스")
    void 정상__프로모션_상태_확인(LocalDateTime start, LocalDateTime end, boolean expected) {
        // given
        Promotion promotion = new Promotion("프로모션", 2, 1, start, end);
        PromotionProduct promotionProduct = new PromotionProduct("콜라", 1000, promotion);

        // when
        boolean isAvailable = promotionProduct.isAvailable();

        // then
        Assertions.assertEquals(expected, isAvailable);
    }

    @Nested
    class 객체_생성_테스트 {
        @Test
        void 정상__객체_생성() {
            // given
            Promotion promotion = new Promotion("프로모션1", 2, 1,
                    LocalDateTimes.now(), LocalDateTimes.now().plusDays(7));
            PromotionProduct promotionProduct = new PromotionProduct("콜라", 1000, promotion);

            // when
            ProductType type = promotionProduct.getType();

            // then
            Assertions.assertEquals(ProductType.PROMOTION, type);
        }
    }

}
