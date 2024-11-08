package store.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import store.entity.product.CommonProduct;
import store.entity.product.Product;
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
            productStock.addProducts(coke, 5);
            productStock.addProducts(sprite, 3);
            productStock.addProducts(orange, 8);

            // then
            assertEquals(5, productStock.getProductQuantity(coke.getName(), coke.getType()));
            assertEquals(3, productStock.getProductQuantity(sprite.getName(), sprite.getType()));
            assertEquals(8, productStock.getProductQuantity(orange.getName(), orange.getType()));
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
            productStock.addProducts(coke, 5);
            productStock.addProducts(sprite, 3);
            productStock.addProducts(orange, 8);

            // when
            ProductStockException exception = assertThrows(ProductStockException.class,
                    () -> productStock.getProductQuantity("밀크커피", coke.getType()));

            // then
            assertEquals(ProductStockExceptionMessage.NOT_EXIST_PRODUCT.getMessage(), exception.getMessage());
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

            productStock.addProducts(coke, 5);
            productStock.addProducts(sprite, 3);
            productStock.addProducts(orange, 8);

            // when
            productStock.reduceProductQuantity(coke.getName(), coke.getType(), 2);
            productStock.reduceProductQuantity(sprite.getName(), sprite.getType(), 1);
            productStock.reduceProductQuantity(orange.getName(), orange.getType(), 3);

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
            productStock.addProducts(coke, 5);
            productStock.addProducts(sprite, 3);
            productStock.addProducts(orange, 8);

            // when
            ProductStockException exception = assertThrows(ProductStockException.class,
                    () -> productStock.reduceProductQuantity(coke.getName(), coke.getType(), 6));

            // then
            assertEquals(ProductStockExceptionMessage.INSUFFICIENT_STOCK.getMessage(), exception.getMessage());
        }

    }

}