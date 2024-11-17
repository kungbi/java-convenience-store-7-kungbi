package store.repository;

import camp.nextstep.edu.missionutils.DateTimes;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import store.domain.Promotion;

class ProductPromotionRepositoryTest {

    @Test
    void existsTest() {
        // given
        ProductPromotionRepository repository = new ProductPromotionRepository();
        Promotion promotion = new Promotion("행사", 2, 1, DateTimes.now().minusDays(1), DateTimes.now());

        // when
        repository.add("콜라", promotion);

        // then
        Assertions.assertTrue(repository.exists("콜라"));
    }

    @Test
    void findTest() {
        // given
        ProductPromotionRepository repository = new ProductPromotionRepository();
        Promotion promotion = new Promotion("행사", 2, 1, DateTimes.now().minusDays(1), DateTimes.now());
        repository.add("콜라", promotion);

        // when
        Optional<Promotion> foundPromotion = repository.findByProductName("콜라");

        // then
        Assertions.assertTrue(foundPromotion.isPresent());
        Assertions.assertEquals(promotion, foundPromotion.get());
    }

}