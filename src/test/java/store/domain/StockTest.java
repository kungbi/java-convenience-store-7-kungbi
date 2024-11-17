package store.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StockTest {

    @Test
    void 정상__재고_추가() {
        //given
        Stock stock = new Stock();

        // when
        stock.add(0, 10);

        // then
        Assertions.assertEquals(10, stock.getQuantity(0));
    }

    @Test
    void 예외__재고_추가() {
        // given
        Stock stock = new Stock();

        // when & then
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> stock.add(0, -1));
    }

    @Test
    void 예외__중복된_id_add() {
        // given
        Stock stock = new Stock();
        stock.add(0, 10);

        // when & then
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> stock.add(0, 20));
    }

    @Test
    void 정상__재고_감소() {
        // given
        Stock stock = new Stock();
        stock.add(0, 10);

        // when
        stock.decrease(0, 5);

        // then
        Assertions.assertEquals(5, stock.getQuantity(0));
    }

    @Test
    void 예외_재고_감소() {
        // given
        Stock stock = new Stock();
        stock.add(0, 10);

        // when & then
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> stock.decrease(0, 11));
    }

}