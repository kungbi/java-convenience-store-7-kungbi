package store.entity.product;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import store.entity.Promotion;

class PromotionProductTest {

    @Nested
    class 객체_생성_테스트 {
        @Test
        void 정상__객체_생성() {
            // given
            Promotion promotion = new Promotion("프로모션1", 2, 1,
                    LocalDateTime.now(), LocalDateTime.now().plusDays(7));
            PromotionProduct promotionProduct = new PromotionProduct("콜라", 1000, promotion);

            // when
            ProductType type = promotionProduct.getType();

            // then
            assertEquals(ProductType.PROMOTION, type);
        }
    }
}
