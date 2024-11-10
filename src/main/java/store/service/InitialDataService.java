package store.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import store.entity.ProductStock;
import store.entity.Promotion;
import store.entity.PromotionManagement;
import store.entity.product.CommonProduct;
import store.entity.product.PromotionProduct;
import store.utils.parsor.CsvReader;
import store.utils.parsor.ProductFieldsDto;
import store.utils.parsor.ProductParser;
import store.utils.parsor.PromotionFieldsDto;
import store.utils.parsor.PromotionParser;

public class InitialDataService {

    public static void init(ProductStock productStock, PromotionManagement promotionManagement)
            throws IOException {
        BufferedReader promotionBufferedReader = new BufferedReader(new FileReader("../resources/promotions.md"));
        PromotionParser promotionParser = new PromotionParser(new CsvReader(promotionBufferedReader, true));

        PromotionFieldsDto promotionFieldsDto;
        while ((promotionFieldsDto = promotionParser.nextPromotion()) != null) {
            promotionManagement.addPromotion(new Promotion(
                    promotionFieldsDto.name(),
                    promotionFieldsDto.buy(),
                    promotionFieldsDto.get(),
                    promotionFieldsDto.startDate(),
                    promotionFieldsDto.endDate()
            ));
        }

        BufferedReader productBufferReader = new BufferedReader(new FileReader("../resources/products.md"));
        ProductParser productParser = new ProductParser(new CsvReader(productBufferReader, true));

        ProductFieldsDto productFieldsDto;
        while ((productFieldsDto = productParser.nextProduct()) != null) {
            if (productFieldsDto.promotionName() == null) {
                productStock.addProduct(new CommonProduct(
                        productFieldsDto.name(),
                        productFieldsDto.price()
                ), productFieldsDto.quantity());
            }
            if (productFieldsDto.promotionName() != null) {
                productStock.addProduct(new PromotionProduct(
                        productFieldsDto.name(),
                        productFieldsDto.price(),
                        promotionManagement.getPromotion(productFieldsDto.promotionName())
                ), productFieldsDto.quantity(
                ));
            }
        }
    }
}