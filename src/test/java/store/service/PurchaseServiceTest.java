package store.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import store.dto.PurchaseInputDto;
import store.entity.ProductStock;
import store.entity.product.CommonProduct;
import store.entity.product.Product;
import store.entity.product.ProductType;

class PurchaseServiceTest {

    @Nested
    class 상품_구매_테스트_ONLY_일반상품 {
        ProductStock productStock;
        PurchaseService purchaseService;

        @BeforeEach
        void setUp() {
            productStock = new ProductStock();
            productStock.addProduct(new CommonProduct("콜라", 1000, 5));
            productStock.addProduct(new CommonProduct("사이다", 2000, 10));
            productStock.addProduct(new CommonProduct("맥주", 3000, 15));
            productStock.addProduct(new CommonProduct("소주", 4000, 20));
            purchaseService = new PurchaseService(productStock);
        }

        @Test
        void 정상__상품_구매() {
            // given
            PurchaseInputDto purchaseInputDto = new PurchaseInputDto("콜라", 5);

            // when
            purchaseService.purchase(purchaseInputDto);

            // then
            Product product = productStock.getProduct("콜라", ProductType.COMMON);
            assertEquals(0, product.getQuantity());

        }
    }

}