package store.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import camp.nextstep.edu.missionutils.DateTimes;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import store.entity.product.CommonProduct;
import store.entity.product.Product;
import store.entity.product.ProductType;
import store.entity.product.PromotionProduct;
import store.exception.ProductStockException;
import store.exception.message.ProductStockExceptionMessage;

class ProductStockTest {

    @Nested
    class 상품_추가_테스트 {

        @Test
        void 정상__상품_추가_후_재고_확인() {
            // given
            ProductStock productStock = new ProductStock();
            Product coke = new CommonProduct("콜라", 1000);
            Product sprite = new CommonProduct("사이다", 1000);
            Product orange = new CommonProduct("오랜지", 1000);

            // when
            productStock.addProduct(coke, 5);
            productStock.addProduct(sprite, 3);
            productStock.addProduct(orange, 8);

            // then
            assertEquals(5, productStock.getProductQuantity(coke.getName(), coke.getType()));
            assertEquals(3, productStock.getProductQuantity(sprite.getName(), sprite.getType()));
            assertEquals(8, productStock.getProductQuantity(orange.getName(), orange.getType()));
        }

        @Test
        void 예외__중복된_상품_추가() {
            // given
            ProductStock productStock = new ProductStock();
            Product coke = new CommonProduct("콜라", 1000);
            productStock.addProduct(coke, 5);

            // when
            Product coke_same = new CommonProduct("콜라", 4000);
            ProductStockException exception = assertThrows(ProductStockException.class,
                    () -> productStock.addProduct(coke_same, 3));

            assertEquals(ProductStockExceptionMessage.DUPLICATE_PRODUCT.getMessage(), exception.getMessage());
        }

        @Test
        void 정상__이름은_같지만_다른_타입() {
            // given
            ProductStock productStock = new ProductStock();
            Product coke = new CommonProduct("콜라", 1000);
            productStock.addProduct(coke, 5);

            // when & then
            Product coke_same = new PromotionProduct("콜라", 4000, new Promotion(
                    "프로모션", 2, 1, DateTimes.now(), DateTimes.now().plusDays(7)
            ));
            productStock.addProduct(coke_same, 3);
            // 에러발생 X
        }

        @Test
        void 예외__상품이_null() {
            // given
            ProductStock productStock = new ProductStock();

            // when
            ProductStockException exception = assertThrows(ProductStockException.class,
                    () -> productStock.addProduct(null, 5));

            // then
            assertEquals(ProductStockExceptionMessage.NULL_PRODUCT.getMessage(), exception.getMessage());
        }
    }

    @Nested
    class 재고_확인_테스트 {

        @Test
        void 예외__없는_상품_확인() {
            // given
            ProductStock productStock = new ProductStock();
            Product coke = new CommonProduct("콜라", 1000);
            Product sprite = new CommonProduct("사이다", 1000);
            Product orange = new CommonProduct("오랜지", 1000);
            productStock.addProduct(coke, 5);
            productStock.addProduct(sprite, 3);
            productStock.addProduct(orange, 8);

            // when
            ProductStockException exception = assertThrows(ProductStockException.class,
                    () -> productStock.getProductQuantity("밀크커피", coke.getType()));

            // then
            assertEquals(ProductStockExceptionMessage.NOT_EXIST_PRODUCT.getMessage(), exception.getMessage());
        }

        @Test
        void 예외__상품_이름이_NULL() {
            // given
            ProductStock productStock = new ProductStock();

            // when
            ProductStockException exception = assertThrows(ProductStockException.class,
                    () -> productStock.getProductQuantity(null, ProductType.COMMON));

            // then
            assertEquals(ProductStockExceptionMessage.NULL_NAME.getMessage(), exception.getMessage());
        }

        @Test
        void 예외__상품_타입이_NULL() {
            // given
            ProductStock productStock = new ProductStock();

            // when
            ProductStockException exception = assertThrows(ProductStockException.class,
                    () -> productStock.getProductQuantity("콜라", null));

            // then
            assertEquals(ProductStockExceptionMessage.NULL_TYPE.getMessage(), exception.getMessage());
        }
    }

    @Nested
    class 재고_감소_테스트 {

        @Test
        void 정상__재고_감소_후_재고_확인() {
            // given
            ProductStock productStock = new ProductStock();
            Product coke = new CommonProduct("콜라", 1000);
            Product sprite = new CommonProduct("사이다", 1000);
            Product orange = new CommonProduct("오랜지", 1000);

            productStock.addProduct(coke, 5);
            productStock.addProduct(sprite, 3);
            productStock.addProduct(orange, 8);

            // when
            productStock.reduceProductQuantity(coke.getName(), 2);
            productStock.reduceProductQuantity(sprite.getName(), 1);
            productStock.reduceProductQuantity(orange.getName(), 3);

            // then
            assertEquals(3, productStock.getProductQuantity(coke.getName(), coke.getType()));
            assertEquals(2, productStock.getProductQuantity(sprite.getName(), sprite.getType()));
            assertEquals(5, productStock.getProductQuantity(orange.getName(), orange.getType()));
        }

        @Test
        void 예외__재고_부족() {
            // given
            Product coke = new CommonProduct("콜라", 1000);
            Product sprite = new CommonProduct("사이다", 1000);
            Product orange = new CommonProduct("오랜지", 1000);
            ProductStock productStock = new ProductStock();
            productStock.addProduct(coke, 5);
            productStock.addProduct(sprite, 3);
            productStock.addProduct(orange, 8);

            // when
            ProductStockException exception = assertThrows(ProductStockException.class,
                    () -> productStock.reduceProductQuantity(coke.getName(), 6));

            // then
            assertEquals(ProductStockExceptionMessage.INSUFFICIENT_STOCK.getMessage(), exception.getMessage());
        }

        @Test
        void 예외__없는_상품_감소() {
            // given
            Product coke = new CommonProduct("콜라", 1000);
            Product sprite = new CommonProduct("사이다", 1000);
            Product orange = new CommonProduct("오랜지", 1000);
            ProductStock productStock = new ProductStock();
            productStock.addProduct(coke, 5);
            productStock.addProduct(sprite, 3);
            productStock.addProduct(orange, 8);

            // when
            ProductStockException exception = assertThrows(ProductStockException.class,
                    () -> productStock.reduceProductQuantity("포도", 6));

            // then
            assertEquals(ProductStockExceptionMessage.NOT_EXIST_PRODUCT.getMessage(), exception.getMessage());
        }

    }

}