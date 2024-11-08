package store.entity.order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class OrderPriceTest {

    static Stream<Arguments> 예외__음수_테스트케이스() {
        return Stream.of(
                Arguments.of(-1, 0, 0),
                Arguments.of(0, -1, 0),
                Arguments.of(0, 0, -1)
        );
    }

    static Stream<Arguments> 정상__최종_결제금액_테스트케이스() {
        return Stream.of(
                Arguments.of(1000, 500, 100, 400),
                Arguments.of(1000, 100, 100, 800),
                Arguments.of(1000, 900, 100, 0),
                Arguments.of(1000, 500, 800, 0)
        );
    }

    @ParameterizedTest
    @MethodSource("예외__음수_테스트케이스")
    void 예외__인자에_음수가_들어오면_예외가_발생한다() {
        assertThrows(IllegalArgumentException.class, () -> {
            new OrderPrice(-1, 0, 0);
        });
    }

    @ParameterizedTest
    @MethodSource("정상__최종_결제금액_테스트케이스")
    void 정상__최종_경제금액(int originalPrice, int promotionDiscount, int membershipDiscount, int expected) {
        // given
        OrderPrice price = new OrderPrice(originalPrice, promotionDiscount, membershipDiscount);

        // when
        int totalPrice = price.calculateTotalPrice();

        // then
        assertEquals(expected, totalPrice);
    }

}