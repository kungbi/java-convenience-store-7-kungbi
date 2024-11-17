package store.repository;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import store.config.ProductType;
import store.domain.Product;

class ProductRepositoryTest {

    @Test
    void add() {
        // given
        ProductRepository productRepository = new ProductRepository();

        // when
        productRepository.add("콜라", 1500, ProductType.COMMON);

        // then
        Assertions.assertEquals(1, productRepository.getSize());
    }

    @Test
    void indexText() {
        // given
        ProductRepository productRepository = new ProductRepository();

        // when
        productRepository.add("콜라", 1500, ProductType.COMMON);
        productRepository.add("바나나", 1500, ProductType.COMMON);

        // then
        Assertions.assertEquals(0,
                productRepository.findByNameAndType("콜라", ProductType.COMMON).get().getId());
        Assertions.assertEquals(1,
                productRepository.findByNameAndType("바나나", ProductType.COMMON).get().getId());
    }

    @Test
    void findByName() {
        // given
        ProductRepository productRepository = new ProductRepository();
        productRepository.add("콜라", 2000, ProductType.COMMON);
        productRepository.add("콜라", 1000, ProductType.PROMOTION);

        // when
        Optional<List<Product>> products = productRepository.findByName("콜라");

        // then
        Assertions.assertTrue(products.isPresent());
        Assertions.assertEquals(2, products.get().size());
    }

    @Test
    void findByNameAndType() {
        // given
        ProductRepository productRepository = new ProductRepository();
        productRepository.add("콜라", 2000, ProductType.COMMON);
        productRepository.add("콜라", 1000, ProductType.PROMOTION);

        // when
        Optional<Product> product = productRepository.findByNameAndType("콜라",
                ProductType.COMMON);

        // then
        Assertions.assertTrue(product.isPresent());
        Assertions.assertEquals("콜라", product.get().getName());
        Assertions.assertEquals(2000, product.get().getPrice());
    }

    @Test
    void getSize() {
        // given
        ProductRepository productRepository = new ProductRepository();
        productRepository.add("콜라", 2000, ProductType.COMMON);
        productRepository.add("콜라", 1000, ProductType.PROMOTION);
        productRepository.add("오렌지", 1000, ProductType.COMMON);

        // when
        int size = productRepository.getSize();

        // then
        Assertions.assertEquals(3, size);
    }

    @Test
    void findAll() {
        // given
        ProductRepository productRepository = new ProductRepository();
        productRepository.add("콜라", 2000, ProductType.COMMON);
        productRepository.add("콜라", 1000, ProductType.PROMOTION);
        productRepository.add("오렌지", 1000, ProductType.COMMON);

        // when
        Optional<List<Product>> products = productRepository.findAll();

        // then
        Assertions.assertTrue(products.isPresent());
        Assertions.assertEquals(3, products.get().size());
    }

    @Test
    void exists() {
    }

    @Test
    void existsWithType() {
    }

}