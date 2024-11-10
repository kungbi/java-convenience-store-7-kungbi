package store.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import store.entity.ProductStock;
import store.entity.Promotion;
import store.entity.PromotionManagement;
import store.entity.product.CommonProduct;
import store.entity.product.PromotionProduct;
import store.utils.parser.CsvReader;
import store.utils.parser.ProductFieldsDto;
import store.utils.parser.ProductParser;
import store.utils.parser.PromotionFieldsDto;
import store.utils.parser.PromotionParser;

public class InitialDataService {

    public static void init(ProductStock productStock, PromotionManagement promotionManagement)
            throws IOException {
        BufferedReader promotionBufferedReader = new BufferedReader(
                new InputStreamReader(InitialDataService.class.getClassLoader().getResourceAsStream("promotions.md"))
        );
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

        BufferedReader productBufferReader = new BufferedReader(
                new InputStreamReader(InitialDataService.class.getClassLoader().getResourceAsStream("products.md"))
        );
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
