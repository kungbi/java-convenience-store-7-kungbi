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
        Product product = new Product("콜라", 1500, ProductType.COMMON);
        productRepository.add(product);

        // then
        Assertions.assertEquals(1, productRepository.getSize());
    }

    @Test
    void findByName() {
        // given
        ProductRepository productRepository = new ProductRepository();
        Product commonProduct = new Product("콜라", 2000, ProductType.COMMON);
        Product promotionProduct = new Product("콜라", 1000, ProductType.PROMOTION);
        productRepository.add(commonProduct);
        productRepository.add(promotionProduct);

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
        Product commonProduct = new Product("콜라", 2000, ProductType.COMMON);
        Product promotionProduct = new Product("콜라", 1000, ProductType.PROMOTION);
        productRepository.add(commonProduct);
        productRepository.add(promotionProduct);

        // when
        Optional<Product> product = productRepository.findByNameAndType(commonProduct.getName(),
                ProductType.COMMON);

        // then
        Assertions.assertTrue(product.isPresent());
        Assertions.assertEquals(commonProduct, product.get());
    }

    @Test
    void getSize() {
        // given
        ProductRepository productRepository = new ProductRepository();
        Product product1 = new Product("콜라", 2000, ProductType.COMMON);
        Product product2 = new Product("콜라", 1000, ProductType.PROMOTION);
        Product product3 = new Product("오렌지", 1000, ProductType.COMMON);
        productRepository.add(product1);
        productRepository.add(product2);
        productRepository.add(product3);

        // when
        int size = productRepository.getSize();

        // then
        Assertions.assertEquals(3, size);
    }

    @Test
    void findAll() {
        // given
        ProductRepository productRepository = new ProductRepository();
        Product product1 = new Product("콜라", 2000, ProductType.COMMON);
        Product product2 = new Product("콜라", 1000, ProductType.PROMOTION);
        Product product3 = new Product("오렌지", 1000, ProductType.COMMON);
        productRepository.add(product1);
        productRepository.add(product2);
        productRepository.add(product3);

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