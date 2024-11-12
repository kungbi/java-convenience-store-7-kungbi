package store.entity.membership;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import store.exception.MembershipException;
import store.exception.message.MembershipExceptionMessage;

class BasicMembershipTest {

    @Test
    void 정상__할인금액_확인() {
        // given
        int originalPrice = 10_000;
        BasicMembership basicMembership = new BasicMembership();

        // when
        int price = basicMembership.calculateDiscountedPrice(originalPrice);

        // then
        assertEquals(3_000, price);
    }

    @Test
    void 정상__할인금액_확인_최대할인금액_적용() {
        // given
        int originalPrice = 100_000;
        BasicMembership basicMembership = new BasicMembership();

        // when
        int price = basicMembership.calculateDiscountedPrice(originalPrice);

        // then
        assertEquals(8_000, price);
    }

    @Test
    void 정상__할인금액이_소수일때_10001() {
        // given
        int originalPrice = 10_001;
        BasicMembership basicMembership = new BasicMembership();

        // when
        int price = basicMembership.calculateDiscountedPrice(originalPrice); // 할인금액 3000.3 -> 3000 (버림)

        // then
        assertEquals(3_000, price);
    }

    @Test
    void 정상__할인금액이_소수일때_10003() {
        // given
        int originalPrice = 10_003;
        BasicMembership basicMembership = new BasicMembership();

        // when
        int price = basicMembership.calculateDiscountedPrice(originalPrice); // 할인금액 3000.9 -> 3000 (버림)

        // then
        assertEquals(3_000, price);
    }

    @Test
    void 에외__음수_가격() {
        // given
        int originalPrice = -1;
        BasicMembership basicMembership = new BasicMembership();

        // when & then
        MembershipException exception = assertThrows(MembershipException.class, () -> {
            basicMembership.calculateDiscountedPrice(originalPrice);
        });

        assertEquals(MembershipExceptionMessage.INVALID_PRICE.getMessage(), exception.getMessage());
    }

}