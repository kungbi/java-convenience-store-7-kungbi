package store.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import store.exception.PromotionException;
import store.exception.message.PromotionExceptionMessage;
import store.utils.Date.LocalDateTimes;

class PromotionTest {

    @Nested
    class 프로모션_생성_테스트 {
        @Test
        void 정상__프로모션_생성() {
            // given
            String name = "프로모션1";
            int buyQuantity = 2;
            int freeQuantity = 1;
            LocalDateTime startDate = LocalDateTimes.now();
            LocalDateTime endDate = LocalDateTimes.now().plusDays(7);

            // when
            Promotion promotion = new Promotion(name, buyQuantity, freeQuantity, startDate, endDate);

            // then
            assertEquals(name, promotion.getName());
            assertEquals(buyQuantity, promotion.getBuyQuantity());
            assertEquals(freeQuantity, promotion.getFreeQuantity());
        }

        @Test
        void 예외__프로모션_이름이_NULL_일_경우() {
            // given
            String name = null;
            int buyQuantity = 2;
            int freeQuantity = 1;
            LocalDateTime startDate = LocalDateTimes.now();
            LocalDateTime endDate = LocalDateTimes.now().plusDays(7);

            // when
            PromotionException exception = Assertions.assertThrows(PromotionException.class,
                    () -> new Promotion(name, buyQuantity, freeQuantity, startDate, endDate));

            // then
            assertEquals(PromotionExceptionMessage.NULL_NAME.getMessage(), exception.getMessage());
        }

        @Test
        void 예외__프로모션_이름이_빈_문자열_일_경우() {
            // given
            String name = "";
            int buyQuantity = 2;
            int freeQuantity = 1;
            LocalDateTime startDate = LocalDateTimes.now();
            LocalDateTime endDate = LocalDateTimes.now().plusDays(7);

            // when
            PromotionException exception = Assertions.assertThrows(PromotionException.class,
                    () -> new Promotion(name, buyQuantity, freeQuantity, startDate, endDate));

            // then
            assertEquals(PromotionExceptionMessage.EMPTY_NAME.getMessage(), exception.getMessage());
        }

        @Test
        void 예외__프로모션_이름이_50자를_초과한_경우() {
            // given
            String name = "123456789012345678901234567890123456789012345678901"; // 51 characters
            int buyQuantity = 2;
            int freeQuantity = 1;
            LocalDateTime startDate = LocalDateTimes.now();
            LocalDateTime endDate = LocalDateTimes.now().plusDays(7);

            // when
            PromotionException exception = Assertions.assertThrows(PromotionException.class,
                    () -> new Promotion(name, buyQuantity, freeQuantity, startDate, endDate));

            // then
            assertEquals(PromotionExceptionMessage.EXCEED_NAME_LENGTH.getMessage(), exception.getMessage());

        }

        @Test
        void 예외__프로모션_시작_날짜에_NULL_값이_들어간_경우() {
            // given
            String name = "프로모션1";
            int buyQuantity = 2;
            int freeQuantity = 1;
            LocalDateTime startDate = null;
            LocalDateTime endDate = LocalDateTimes.now().plusDays(7);

            // when
            PromotionException exception = Assertions.assertThrows(PromotionException.class,
                    () -> new Promotion(name, buyQuantity, freeQuantity, startDate, endDate));

            // then
            assertEquals(PromotionExceptionMessage.NULL_DATE.getMessage(), exception.getMessage());
        }

        @Test
        void 예외__프로모션_종료_날짜에_NULL_값이_들어간_경우() {
            // given
            String name = "프로모션1";
            int buyQuantity = 2;
            int freeQuantity = 1;
            LocalDateTime startDate = LocalDateTimes.now();
            LocalDateTime endDate = null;

            // when
            PromotionException exception = Assertions.assertThrows(PromotionException.class,
                    () -> new Promotion(name, buyQuantity, freeQuantity, startDate, endDate));

            // then
            assertEquals(PromotionExceptionMessage.NULL_DATE.getMessage(), exception.getMessage());
        }

        @Test
        void 예외__구매_개수가_1보다_작을_경우() {
            // given
            String name = "프로모션1";
            int buyQuantity = 0;
            int freeQuantity = 10;
            LocalDateTime startDate = LocalDateTimes.now();
            LocalDateTime endDate = LocalDateTimes.now().plusDays(7);

            // when
            PromotionException exception = Assertions.assertThrows(PromotionException.class,
                    () -> new Promotion(name, buyQuantity, freeQuantity, startDate, endDate));

            // then
            assertEquals(PromotionExceptionMessage.BUY_QUANTITY_MIN.getMessage(), exception.getMessage());
        }


        @Test
        void 예외__증정_개수가_1보다_작을_경우() {
            // given
            String name = "프로모션1";
            int buyQuantity = 10;
            int freeQuantity = 0;
            LocalDateTime startDate = LocalDateTimes.now();
            LocalDateTime endDate = LocalDateTimes.now().plusDays(7);

            // when
            PromotionException exception = Assertions.assertThrows(PromotionException.class,
                    () -> new Promotion(name, buyQuantity, freeQuantity, startDate, endDate));

            // then
            assertEquals(PromotionExceptionMessage.FREE_QUANTITY_MIN.getMessage(), exception.getMessage());
        }

        @Test
        void 예외__프로모션_시작일은_종료일보다_이전이어야_한다() {
            // given
            String name = "프로모션1";
            int buyQuantity = 2;
            int freeQuantity = 1;
            LocalDateTime startDate = LocalDateTimes.now().plusDays(7);
            LocalDateTime endDate = LocalDateTimes.now();

            // when
            PromotionException exception = Assertions.assertThrows(PromotionException.class,
                    () -> new Promotion(name, buyQuantity, freeQuantity, startDate, endDate));

            // then
            assertEquals(PromotionExceptionMessage.DATE_ORDER.getMessage(), exception.getMessage());
        }

    }


    @Nested
    class 프로모션_기능_테스트 {

        @Test
        void 정상__현재_적용_가능한_프로모션인지_확인_기능() {
            // given
            String name = "프로모션1";
            int buyQuantity = 2;
            int freeQuantity = 1;
            LocalDateTime startDate = LocalDateTimes.now().minusDays(1);
            LocalDateTime endDate = LocalDateTimes.now().plusDays(7);
            Promotion promotion = new Promotion(name, buyQuantity, freeQuantity, startDate, endDate);

            // when
            boolean isAvailable = promotion.isAvailable();

            // then
            assertEquals(true, isAvailable);
        }

    }

}