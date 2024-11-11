package store.service.ProductStockService;

import java.util.ArrayList;
import java.util.List;
import store.dto.ProductInfoDto;
import store.entity.ProductStock;
import store.entity.Promotion;
import store.entity.product.Product;
import store.entity.product.ProductType;
import store.entity.product.PromotionProduct;

public class ProductInfoService {
    private final ProductStock productStock;

    public ProductInfoService(ProductStock productStock) {
        this.productStock = productStock;
    }

    public List<ProductInfoDto> getProductsInformation() {
        List<ProductInfoDto> productInfoDtos = new ArrayList<>();
        for (Product product : productStock.getProducts()) {
            addProductInfoDto(productInfoDtos, product);
        }
        return productInfoDtos;
    }


    private void addProductInfoDto(List<ProductInfoDto> productInfoDtos, Product product) {
        int quantity = productStock.getProductQuantityByUuid(product.getUuid());
        String promotionName = getPromotionNameIfApplicable(product);
        productInfoDtos.add(new ProductInfoDto(product.getName(), product.getPrice(), quantity, promotionName));
    }


    private String getPromotionNameIfApplicable(Product product) {
        if (product.getType() == ProductType.PROMOTION) {
            Promotion promotion = ((PromotionProduct) product).getPromotion();
            return promotion.getName();
        }
        return null;
    }
}
