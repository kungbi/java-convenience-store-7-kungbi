package store.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import store.entity.product.CommonProduct;
import store.entity.product.Product;
import store.exception.ProductStockException;
import store.exception.message.InventoryExceptionMessage;

class ProductStockTest {

    @Nested
    class 상품_추가_테스트 {

        @Test
        void 정상__상품_추가() {
            // given
            ProductStock productStock = new ProductStock();
            Product product = new CommonProduct("콜라", 1000);

            // when
            productStock.addProduct(product);

            // then
            Product foundProduct = productStock.getProduct("콜라", product.getType());
            assertEquals(product.getName(), foundProduct.getName());
        }

        @Test
        void 정상__상품_추가_두개() {
            // given
            ProductStock productStock = new ProductStock();
            Product product1 = new CommonProduct("콜라", 1000);
            Product product2 = new CommonProduct("사이다", 1000);

            // when
            productStock.addProduct(product1);
            productStock.addProduct(product2);

            // then
            Product foundProduct1 = productStock.getProduct("콜라", product1.getType());
            Product foundProduct2 = productStock.getProduct("사이다", product2.getType());
            assertEquals(product1.getName(), foundProduct1.getName());
            assertEquals(product2.getName(), foundProduct2.getName());
        }

        @Test
        void 예외__상품_NULL() {
            // given
            ProductStock productStock = new ProductStock();
            Product product = null;

            // when & then
            ProductStockException exception = assertThrows(ProductStockException.class,
                    () -> productStock.addProduct(product));

            assertEquals(InventoryExceptionMessage.NULL_PRODUCT.getMessage(), exception.getMessage());
            assertEquals(NullPointerException.class, exception.getCause().getClass());
        }

        @Test
        void 예외__중복된_상품_추가() {
            // given
            ProductStock productStock = new ProductStock();
            Product product1 = new CommonProduct("콜라", 1000);
            Product product2 = new CommonProduct("콜라", 2000);

            // when
            productStock.addProduct(product1);

            // when & then
            ProductStockException exception = assertThrows(ProductStockException.class,
                    () -> productStock.addProduct(product2));

            assertEquals(InventoryExceptionMessage.DUPLICATE_PRODUCT.getMessage(), exception.getMessage());
        }

    }

}