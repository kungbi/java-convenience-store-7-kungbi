package store;

import store.controller.InputRetryUtil;
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
        StoreController storeController = configureDependencies();
        storeController.run();
    }

    private static StoreController configureDependencies() {
        ProductStock productStock = new ProductStock();
        ConsoleInput consoleInput = new ConsoleInput();
        ConsoleOutput consoleOutput = new ConsoleOutput();
        InitialDataService.init(productStock, new PromotionManagement());
        ProductStockService productStockService = new ProductStockService(productStock);
        PromotionService promotionService = new PromotionService(productStock);

        return createController(consoleInput, consoleOutput, productStockService, promotionService,
                new PurchaseService(productStockService, promotionService, new BasicMembership()),
                new InputRetryUtil(consoleInput, consoleOutput, productStockService));
    }

    private static StoreController createController(ConsoleInput consoleInput, ConsoleOutput consoleOutput,
                                                    ProductStockService productStockService,
                                                    PromotionService promotionService, PurchaseService purchaseService,
                                                    InputRetryUtil inputRetryUtil) {
        return new StoreController(consoleInput, consoleOutput, productStockService, promotionService, purchaseService,
                inputRetryUtil);
    }
}
