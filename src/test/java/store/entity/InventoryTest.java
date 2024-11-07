package store.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import store.entity.product.CommonProduct;
import store.entity.product.Product;
import store.entity.product.ProductType;
import store.exception.InventoryException;
import store.exception.message.InventoryExceptionMessage;

class InventoryTest {

    @Nested
    class 상품_추가_테스트 {

        @Test
        void 정상__상품_추가() {
            // given
            Inventory inventory = new Inventory();
            Product product = new CommonProduct("콜라", 1000, 10);

            // when
            inventory.addProduct(product);

            // then
            Product foundProduct = inventory.getProduct("콜라", product.getType());
            assertEquals(product.getName(), foundProduct.getName());
        }

        @Test
        void 정상__상품_추가_두개() {
            // given
            Inventory inventory = new Inventory();
            Product product1 = new CommonProduct("콜라", 1000, 10);
            Product product2 = new CommonProduct("사이다", 1000, 10);

            // when
            inventory.addProduct(product1);
            inventory.addProduct(product2);

            // then
            Product foundProduct1 = inventory.getProduct("콜라", product1.getType());
            Product foundProduct2 = inventory.getProduct("사이다", product2.getType());
            assertEquals(product1.getName(), foundProduct1.getName());
            assertEquals(product2.getName(), foundProduct2.getName());
        }

        @Test
        void 예외__상품_NULL() {
            // given
            Inventory inventory = new Inventory();
            Product product = null;

            // when & then
            InventoryException exception = assertThrows(InventoryException.class,
                    () -> inventory.addProduct(product));

            assertEquals(InventoryExceptionMessage.NULL_PRODUCT.getMessage(), exception.getMessage());
            assertEquals(NullPointerException.class, exception.getCause().getClass());
        }

        @Test
        void 예외__중복된_상품_추가() {
            // given
            Inventory inventory = new Inventory();
            Product product1 = new CommonProduct("콜라", 1000, 10);
            Product product2 = new CommonProduct("콜라", 2000, 10);

            // when
            inventory.addProduct(product1);

            // when & then
            InventoryException exception = assertThrows(InventoryException.class,
                    () -> inventory.addProduct(product2));

            assertEquals(InventoryExceptionMessage.DUPLICATE_PRODUCT.getMessage(), exception.getMessage());
        }

    }

    @Nested
    class 상품_확인_테스트 {
        @Test
        void 정상__상품_가져오기() {
            // given
            Inventory inventory = new Inventory();
            Product product = new CommonProduct("콜라", 1000, 10);
            inventory.addProduct(product);

            // when
            Product foundProduct = inventory.getProduct("콜라", product.getType());
            product.buy(1);

            // then
            assertEquals(10, foundProduct.getQuantity());
        }

        @Test
        void 예외__상품_가져오기_이름이_NULL() {
            // given
            Inventory inventory = new Inventory();
            String productName = null;
            ProductType productType = ProductType.COMMON;

            // when & then
            InventoryException exception = assertThrows(InventoryException.class,
                    () -> inventory.getProduct(productName, productType));

            assertEquals(InventoryExceptionMessage.NULL_NAME.getMessage(), exception.getMessage());
        }

        @Test
        void 예외__상품_가져오기_TYPE이_NULL() {
            // given
            Inventory inventory = new Inventory();
            String productName = "콜라";
            ProductType productType = null;

            // when & then
            InventoryException exception = assertThrows(InventoryException.class,
                    () -> inventory.getProduct(productName, productType));

            assertEquals(InventoryExceptionMessage.NULL_TYPE.getMessage(), exception.getMessage());
        }

        @Test
        void 예외__없는_상품_조회() {
            // given
            Inventory inventory = new Inventory();
            Product product = new CommonProduct("콜라", 1000, 10);
            inventory.addProduct(product);

            // when & then
            InventoryException exception = assertThrows(InventoryException.class,
                    () -> inventory.getProduct("사이다", product.getType()));

            assertEquals(InventoryExceptionMessage.NOT_EXIST_PRODUCT.getMessage(), exception.getMessage());
        }
    }

}