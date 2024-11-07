package store.entity.product;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import store.exception.ProductException;

public class CommonProductTest {

    @Nested
    class 상품_생성_테스트 {

        @Test
        void 정상__상품_생성() {
            // given
            String name = "콜라";
            int price = 1000;
            int quantity = 10;

            // when
            CommonProduct commonProduct = new CommonProduct(name, price, quantity);

            // then
            assertEquals(name, commonProduct.getName());
            assertEquals(price, commonProduct.getPrice());
            assertEquals(quantity, commonProduct.getQuantity());
        }

        @Test
        void 예외__상품_이름이_null인_경우() {
            // given
            String name = null;
            int price = 1000;
            int quantity = 10;

            // when & then
            ProductException exception = Assertions.assertThrows(ProductException.class,
                    () -> new CommonProduct(name, price, quantity));

            assertEquals("상품 이름이 NULL 입니다.", exception.getMessage());
        }

        @Test
        void 예외__상품_이름이_빈문자열인_경우() {
            // given
            String name = "";
            int price = 1000;
            int quantity = 10;

            // when & then
            ProductException exception = Assertions.assertThrows(ProductException.class,
                    () -> new CommonProduct(name, price, quantity));

            assertEquals("상품 이름이 빈 문자열입니다.", exception.getMessage());
        }

        @Test
        void 예외__상품_이름이_50자_초과인_경우() {
            // given
            String name = "123456789012345678901234567890123456789012345678901"; // 51 characters
            int price = 1000;
            int quantity = 10;

            // when & then
            ProductException exception = Assertions.assertThrows(ProductException.class,
                    () -> new CommonProduct(name, price, quantity));

            assertEquals("상품 이름이 50자를 초과했습니다.", exception.getMessage());
        }

        @Test
        void 예외__상품_가격이_0이하일_경우() {
            // given
            String name = "콜라";
            int price = 0;
            int quantity = 10;

            // when & then
            ProductException exception = Assertions.assertThrows(ProductException.class,
                    () -> new CommonProduct(name, price, quantity));

            assertEquals("상품 가격이 0원 이하입니다.", exception.getMessage());
        }

        @Test
        void 예외__상품_수량이_0보다_작은경우() {
            // given
            String name = "콜라";
            int price = 1000;
            int quantity = -1;

            // when & then
            ProductException exception = Assertions.assertThrows(ProductException.class,
                    () -> new CommonProduct(name, price, quantity));

            assertEquals("상품 수량이 0개 미만입니다.", exception.getMessage());
        }


    }

    @Nested
    class 상품_기능_테스트 {
        @Test
        void 정상__상품_구매_기능() {
            // given
            String name = "콜라";
            int price = 1000;
            int quantity = 10;
            CommonProduct commonProduct = new CommonProduct(name, price, quantity);

            // when
            commonProduct.buy(5);

            // then
            assertEquals(5, commonProduct.getQuantity());
        }

        @Test
        void 예외__상품_재고가_부족한_경우() {
            // given
            String name = "콜라";
            int price = 1000;
            int quantity = 10;
            CommonProduct commonProduct = new CommonProduct(name, price, quantity);

            // when & then
            ProductException exception = Assertions.assertThrows(ProductException.class, () -> commonProduct.buy(11));

            assertEquals("상품 재고가 부족합니다.", exception.getMessage());
        }

        @Test
        void 정상__상품_재고_확인_기능() {
            // given
            String name = "콜라";
            int price = 1000;
            int quantity = 10;
            CommonProduct commonProduct = new CommonProduct(name, price, quantity);

            // when
            boolean isAvailable = commonProduct.hasSufficientStock(5);

            // then
            assertEquals(true, isAvailable);
        }

        @Test
        void 정상__상품_이름_비교() {
            // given
            CommonProduct lowerCaseCommonProduct = new CommonProduct("coke", 1000, 10);
            CommonProduct upperCaseCommonProduct = new CommonProduct("COKE", 1000, 10);

            // when
            boolean isSameName = lowerCaseCommonProduct.isSameName(upperCaseCommonProduct);

            // then
            assertEquals(true, isSameName);
        }
    }
}
