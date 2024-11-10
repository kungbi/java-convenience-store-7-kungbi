package store;

import java.io.IOException;
import store.controller.StoreController;
import store.entity.ProductStock;
import store.entity.PromotionManagement;
import store.entity.membership.BasicMembership;
import store.service.InitialDataService;
import store.service.ProductStockService;
import store.service.PromotionService;
import store.service.PurchaseService;
import store.view.ConsoleInput;
import store.view.ConsoleOutput;

public class Application {
    public static void main(String[] args) {
        ProductStock productStock = new ProductStock();
        PromotionManagement promotionManagement = new PromotionManagement();
        ConsoleInput consoleInput = new ConsoleInput();
        ConsoleOutput consoleOutput = new ConsoleOutput();

        try {
            InitialDataService.init(productStock, promotionManagement);
        } catch (IOException error) {
            System.err.println("Error while initializing data: " + error.getMessage());
            error.printStackTrace();  // 추가: 예외 내용을 출력합니다.
            throw new RuntimeException("Error while initializing data", error);
        }

        ProductStockService productStockService = new ProductStockService(productStock);
        PromotionService promotionService = new PromotionService(productStock);
        BasicMembership basicMembership = new BasicMembership();
        PurchaseService purchaseService = new PurchaseService(productStockService, promotionService, basicMembership);
        new PurchaseService(productStockService, promotionService, basicMembership);

        StoreController storeController = new StoreController(
                consoleInput,
                consoleOutput,
                productStockService,
                promotionService,
                purchaseService
        );

        storeController.run();

    }
}
