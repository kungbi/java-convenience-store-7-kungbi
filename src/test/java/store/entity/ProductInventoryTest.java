package store.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import store.entity.product.CommonProduct;
import store.entity.product.Product;
import store.exception.ProductInventoryException;
import store.exception.message.ProductInventoryExceptionMessage;

class ProductInventoryTest {

    @Nested
    class 상품_추가_테스트 {

        @Test
        void 정상__상품_추가() {
            // given
            ProductInventory productInventory = new ProductInventory();
            Product product = new CommonProduct("콜라", 1000, 10);

            // when
            productInventory.addProduct(product);

            // then
            Product foundProduct = productInventory.getProduct("콜라", product.getType());
            assertEquals(product.getName(), foundProduct.getName());
        }

        @Test
        void 정상__상품_추가_두개() {
            // given
            ProductInventory productInventory = new ProductInventory();
            Product product1 = new CommonProduct("콜라", 1000, 10);
            Product product2 = new CommonProduct("사이다", 1000, 10);

            // when
            productInventory.addProduct(product1);
            productInventory.addProduct(product2);

            // then
            Product foundProduct1 = productInventory.getProduct("콜라", product1.getType());
            Product foundProduct2 = productInventory.getProduct("사이다", product2.getType());
            assertEquals(product1.getName(), foundProduct1.getName());
            assertEquals(product2.getName(), foundProduct2.getName());
        }

        @Test
        void 예외__상품_NULL() {
            // given
            ProductInventory productInventory = new ProductInventory();
            Product product = null;

            // when & then
            ProductInventoryException exception = assertThrows(ProductInventoryException.class,
                    () -> productInventory.addProduct(product));

            assertEquals(ProductInventoryExceptionMessage.NULL_PRODUCT.getMessage(), exception.getMessage());
            assertEquals(NullPointerException.class, exception.getCause().getClass());
        }

        @Test
        void 예외__중복된_상품_추가() {
            // given
            ProductInventory productInventory = new ProductInventory();
            Product product1 = new CommonProduct("콜라", 1000, 10);
            Product product2 = new CommonProduct("콜라", 2000, 10);

            // when
            productInventory.addProduct(product1);

            // when & then
            ProductInventoryException exception = assertThrows(ProductInventoryException.class,
                    () -> productInventory.addProduct(product2));

            assertEquals(ProductInventoryExceptionMessage.DUPLICATE_PRODUCT.getMessage(), exception.getMessage());
        }

    }

    @Nested
    class 상품_확인_테스트 {
        @Test
        void 정상__상품_가져오기() {
            // given
            ProductInventory productInventory = new ProductInventory();
            Product product = new CommonProduct("콜라", 1000, 10);
            productInventory.addProduct(product);

            // when
            Product foundProduct = productInventory.getProduct("콜라", product.getType());
            product.buy(1);

            // then
            assertEquals(10, foundProduct.getQuantity());
        }
    }

}