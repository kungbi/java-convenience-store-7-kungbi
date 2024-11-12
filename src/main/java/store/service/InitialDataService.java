package store.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import store.Configuration;
import store.entity.ProductStock;
import store.entity.Promotion;
import store.entity.PromotionManagement;
import store.entity.product.CommonProduct;
import store.entity.product.Product;
import store.entity.product.ProductType;
import store.entity.product.PromotionProduct;
import store.utils.parser.CsvReader;
import store.utils.parser.ProductFieldsDto;
import store.utils.parser.ProductParser;
import store.utils.parser.PromotionFieldsDto;
import store.utils.parser.PromotionParser;

public class InitialDataService {

    public static final String ERROR_WHILE_INITIALIZING_DATA = "Error while initializing data";

    public static void init(ProductStock productStock, PromotionManagement promotionManagement) {
        try {
            loadPromotions(promotionManagement);
            loadProducts(productStock, promotionManagement);
            addMissingCommonProducts(productStock);
        } catch (IOException error) {
            throw new IllegalArgumentException(ERROR_WHILE_INITIALIZING_DATA, error);
        }
    }

    private static void loadPromotions(PromotionManagement promotionManagement) throws IOException {
        BufferedReader reader = createBufferedReader(Configuration.PROMOTION_DATA_FILE.getString());
        PromotionParser parser = new PromotionParser(new CsvReader(reader, true));

        PromotionFieldsDto fields;
        while ((fields = parser.nextPromotion()) != null) {
            promotionManagement.addPromotion(createPromotion(fields));
        }
    }

    private static void loadProducts(ProductStock productStock, PromotionManagement promotionManagement)
            throws IOException {
        BufferedReader reader = createBufferedReader(Configuration.PRODUCT_DATA_FILE.getString());
        ProductParser parser = new ProductParser(new CsvReader(reader, true));

        ProductFieldsDto fields;
        while ((fields = parser.nextProduct()) != null) {
            addProductBasedOnType(productStock, promotionManagement, fields);
        }
    }

    private static BufferedReader createBufferedReader(String fileName) {
        return new BufferedReader(new InputStreamReader(
                InitialDataService.class.getClassLoader().getResourceAsStream(fileName)
        ));
    }

    private static Promotion createPromotion(PromotionFieldsDto fields) {
        return new Promotion(fields.name(), fields.buy(), fields.get(), fields.startDate(), fields.endDate());
    }

    private static void addProductBasedOnType(ProductStock productStock, PromotionManagement promotionManagement,
                                              ProductFieldsDto fields) {
        if (fields.promotionName() == null) {
            addCommonProduct(productStock, fields);
            return;
        }
        addPromotionProduct(productStock, promotionManagement, fields);
    }

    private static void addCommonProduct(ProductStock productStock, ProductFieldsDto fields) {
        productStock.addProduct(new CommonProduct(fields.name(), fields.price()), fields.quantity());
    }

    private static void addPromotionProduct(ProductStock productStock, PromotionManagement promotionManagement,
                                            ProductFieldsDto fields) {
        Promotion promotion = promotionManagement.getPromotion(fields.promotionName());
        PromotionProduct product = new PromotionProduct(fields.name(), fields.price(), promotion);
        productStock.addProduct(product, fields.quantity());
    }

    private static void addMissingCommonProducts(ProductStock productStock) {
        for (Product product : productStock.getProducts()) {
            if (!productStock.isExistProductWithType(product.getName(), ProductType.COMMON)) {
                productStock.addProduct(new CommonProduct(product.getName(), product.getPrice()), 0);
            }
        }
    }
}
