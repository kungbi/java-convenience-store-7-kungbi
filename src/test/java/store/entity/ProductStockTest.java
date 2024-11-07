package store.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Stream;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import store.entity.product.CommonProduct;
import store.entity.product.Product;
import store.entity.product.ProductType;
import store.exception.ProductException;
import store.exception.ProductStockException;
import store.exception.message.InventoryExceptionMessage;
import store.exception.message.ProductExceptionMessage;

class ProductStockTest {

    @Nested
    class 상품_추가_테스트 {

        @Test
        void 정상__상품_추가() {
            // given
            ProductStock productStock = new ProductStock();
            Product product = new CommonProduct("콜라", 1000, 10);

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
            Product product1 = new CommonProduct("콜라", 1000, 10);
            Product product2 = new CommonProduct("사이다", 1000, 10);

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
            Product product1 = new CommonProduct("콜라", 1000, 10);
            Product product2 = new CommonProduct("콜라", 2000, 10);

            // when
            productStock.addProduct(product1);

            // when & then
            ProductStockException exception = assertThrows(ProductStockException.class,
                    () -> productStock.addProduct(product2));

            assertEquals(InventoryExceptionMessage.DUPLICATE_PRODUCT.getMessage(), exception.getMessage());
        }

    }

    @Nested
    class 상품_확인_테스트 {
        @Test
        void 정상__상품_가져오기() {
            // given
            ProductStock productStock = new ProductStock();
            Product product = new CommonProduct("콜라", 1000, 10);
            productStock.addProduct(product);

            // when
            Product foundProduct = productStock.getProduct("콜라", product.getType());
            product.buy(1);

            // then
            assertEquals(10, foundProduct.getQuantity());
        }

        @Test
        void 예외__상품_가져오기_이름이_NULL() {
            // given
            ProductStock productStock = new ProductStock();
            String productName = null;
            ProductType productType = ProductType.COMMON;

            // when & then
            ProductStockException exception = assertThrows(ProductStockException.class,
                    () -> productStock.getProduct(productName, productType));

            assertEquals(InventoryExceptionMessage.NULL_NAME.getMessage(), exception.getMessage());
        }

        @Test
        void 예외__상품_가져오기_TYPE이_NULL() {
            // given
            ProductStock productStock = new ProductStock();
            String productName = "콜라";
            ProductType productType = null;

            // when & then
            ProductStockException exception = assertThrows(ProductStockException.class,
                    () -> productStock.getProduct(productName, productType));

            assertEquals(InventoryExceptionMessage.NULL_TYPE.getMessage(), exception.getMessage());
        }

        @Test
        void 예외__없는_상품_조회() {
            // given
            ProductStock productStock = new ProductStock();
            Product product = new CommonProduct("콜라", 1000, 10);
            productStock.addProduct(product);

            // when & then
            ProductStockException exception = assertThrows(ProductStockException.class,
                    () -> productStock.getProduct("사이다", product.getType()));

            assertEquals(InventoryExceptionMessage.NOT_EXIST_PRODUCT.getMessage(), exception.getMessage());
        }
    }

    @Nested
    class 상품_수량_조작_테스트 {
        static Stream<Arguments> 정상__상품_감소_테스트케이스() {
            return Stream.of(
                    Arguments.of(10, 10, 0),
                    Arguments.of(10, 5, 5),
                    Arguments.of(15, 5, 10)
            );
        }

        static Stream<Arguments> 예외__상품_감소_테스트케이스() {
            return Stream.of(
                    Arguments.of(0, 1),
                    Arguments.of(3, 4),
                    Arguments.of(10, 100)
            );
        }


        @ParameterizedTest
        @MethodSource("정상__상품_감소_테스트케이스")
        void 정상__상품_감소(int quantity, int decreaseQuantity, int expected) {
            // given
            ProductStock productStock = new ProductStock();
            Product product = new CommonProduct("콜라", 1000, quantity);
            productStock.addProduct(product);

            // when
            productStock.reduceQuantity("콜라", product.getType(), decreaseQuantity);
            Product foundProduct = productStock.getProduct("콜라", product.getType());

            // then
            assertEquals(expected, foundProduct.getQuantity());
        }

        @ParameterizedTest
        @MethodSource("예외__상품_감소_테스트케이스")
        void 예외__상품_감소_테스트(int quantity, int decreaseQuantity) {
            // given
            ProductStock productStock = new ProductStock();
            Product product = new CommonProduct("콜라", 1000, quantity);
            productStock.addProduct(product);

            // when & then
            ProductException exception = assertThrows(ProductException.class,
                    () -> productStock.reduceQuantity("콜라", product.getType(), decreaseQuantity));

            assertEquals(ProductExceptionMessage.INSUFFICIENT_STOCK.getMessage(), exception.getMessage());
        }

        @Test
        void 예외__상품_감소_테스트_이름이_NULL() {
            // given
            ProductStock productStock = new ProductStock();
            String productName = null;
            ProductType productType = ProductType.COMMON;
            int decreaseQuantity = 1;

            // when & then
            ProductStockException exception = assertThrows(ProductStockException.class,
                    () -> productStock.reduceQuantity(productName, productType, decreaseQuantity));

            assertEquals(InventoryExceptionMessage.NULL_NAME.getMessage(), exception.getMessage());
        }

        @Test
        void 예외__상품_감소_테스트_TYPE이_NULL() {
            // given
            ProductStock productStock = new ProductStock();
            String productName = "콜라";
            ProductType productType = null;
            int decreaseQuantity = 1;

            // when & then
            ProductStockException exception = assertThrows(ProductStockException.class,
                    () -> productStock.reduceQuantity(productName, productType, decreaseQuantity));

            assertEquals(InventoryExceptionMessage.NULL_TYPE.getMessage(), exception.getMessage());
        }

        @Test
        void 예외__상품_감소_테스트_감소수량이_음수() {
            // given
            ProductStock productStock = new ProductStock();
            Product product = new CommonProduct("콜라", 1000, 10);
            productStock.addProduct(product);
            int decreaseQuantity = -1;

            // when & then
            ProductStockException exception = assertThrows(ProductStockException.class,
                    () -> productStock.reduceQuantity("콜라", product.getType(), decreaseQuantity));

            assertEquals(InventoryExceptionMessage.INVALID_QUANTITY.getMessage(), exception.getMessage());
        }
    }
}