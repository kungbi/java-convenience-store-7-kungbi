package store.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import store.entity.product.CommonProduct;
import store.entity.product.Product;

class ProductInventoryTest {

    @Nested
    class 인벤토리_생성_테스트 {

    }

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