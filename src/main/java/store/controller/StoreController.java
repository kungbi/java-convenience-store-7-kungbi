package store.controller;

import java.util.ArrayList;
import java.util.List;
import store.dto.ItemDto;
import store.dto.PurchaseItemsDto;
import store.dto.PurchaseRequestDto;
import store.dto.PurchaseResultDto;
import store.service.ProductStockService;
import store.service.PromotionService;
import store.service.PurchaseService;
import store.view.ConsoleInput;
import store.view.ConsoleOutput;

public class StoreController {
    private final ConsoleInput consoleInput;
    private final ConsoleOutput consoleOutput;
    private final ProductStockService productStockService;
    private final PromotionService promotionService;
    private final PurchaseService purchaseService;

    public StoreController(
            ConsoleInput consoleInput,
            ConsoleOutput consoleOutput,
            ProductStockService productStockService,
            PromotionService promotionService,
            PurchaseService purchaseService) {
        this.consoleInput = consoleInput;
        this.consoleOutput = consoleOutput;
        this.productStockService = productStockService;
        this.promotionService = promotionService;
        this.purchaseService = purchaseService;
    }

    public void run() {
        consoleOutput.printWelcomeMessage();
        consoleOutput.printProductList(productStockService.getProductsInformation());

        String purchaseItemsString = consoleInput.getPurchaseItems();

        List<ItemDto> purchaseItems = InputParser.parseItems(purchaseItemsString);

        productStockService.validateStocks(new PurchaseItemsDto(
                purchaseItems
        ));

        for (ItemDto item : promotionService.findAdditionalFreeItems(new PurchaseItemsDto(purchaseItems)).products()) {
            String answer = consoleInput.askForAdditionalPromotion(item.name());
            if (answer.equals("Y")) {
                purchaseItems = this.editItemDtos(purchaseItems, item.name(), item.quantity());
            }
        }

        for (ItemDto item : promotionService.findExcludedPromotionItems(new PurchaseItemsDto(purchaseItems)).items()) {
            consoleInput.askForFullPricePurchase(item.name(), item.quantity());
        }

        PurchaseResultDto purchase = purchaseService.purchase(new PurchaseRequestDto(
                purchaseItems, false
        ));

        consoleOutput.printPurchaseSummary(purchase);


    }

    private List<ItemDto> editItemDtos(List<ItemDto> itemDtos, String name, int quantity) {
        List<ItemDto> newItems = new ArrayList<>();

        for (ItemDto itemDto : itemDtos) {
            if (itemDto.name().equals(name)) {
                newItems.add(new ItemDto(name, itemDto.quantity() + quantity));
            }
            if (!itemDto.name().equals(name)) {
                newItems.add(itemDto);
            }
        }
        return newItems;
    }
}
