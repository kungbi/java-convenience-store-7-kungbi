package store.controller;

import java.util.ArrayList;
import java.util.List;
import store.dto.ItemDto;
import store.dto.PurchaseItemsDto;
import store.dto.PurchaseRequestDto;
import store.dto.PurchaseResultDto;
import store.service.ProductStockService.ProductStockService;
import store.service.PromotionService.PromotionService;
import store.service.PurchaseService.PurchaseService;
import store.view.ConsoleInput;
import store.view.ConsoleOutput;

public class StoreController {
    private final ConsoleInput consoleInput;
    private final ConsoleOutput consoleOutput;
    private final ProductStockService productStockService;
    private final PromotionService promotionService;
    private final PurchaseService purchaseService;
    private final InputRetryUtil inputRetryUtil;

    public StoreController(ConsoleInput consoleInput, ConsoleOutput consoleOutput,
                           ProductStockService productStockService, PromotionService promotionService,
                           PurchaseService purchaseService, InputRetryUtil inputRetryUtil) {
        this.consoleInput = consoleInput;
        this.consoleOutput = consoleOutput;
        this.productStockService = productStockService;
        this.promotionService = promotionService;
        this.purchaseService = purchaseService;
        this.inputRetryUtil = inputRetryUtil;
    }

    public void run() {
        while (true) {
            if (processPurchaseLoop()) {
                break;
            }
        }
    }

    // private methods

    private boolean processPurchaseLoop() {
        try {
            startPurchaseProcess();
            boolean answer = inputRetryUtil.askForAdditionalPurchase();
            if (!answer) {
                return true;
            }
        } catch (IllegalArgumentException error) {
            consoleOutput.printException(error);
        }
        return false;
    }

    private void startPurchaseProcess() {
        displayProducts();
        List<ItemDto> purchaseItems = inputRetryUtil.getPurchaseItems();
        purchaseItems = applyAdditionalPromotions(purchaseItems);
        if (excludedPromotionItemInform(purchaseItems)) {
            return;
        }
        boolean membership = inputRetryUtil.askForMembershipDiscount();
        PurchaseResultDto purchaseResult = purchaseService.purchase(new PurchaseRequestDto(purchaseItems, membership));
        consoleOutput.printPurchaseSummary(purchaseResult);
    }

    private void displayProducts() {
        consoleOutput.printWelcomeMessage();
        consoleOutput.printProductList(productStockService.getProductsInformation());
    }

    private boolean excludedPromotionItemInform(List<ItemDto> purchaseItems) {
        for (ItemDto item : promotionService.findExcludedPromotionItems(new PurchaseItemsDto(purchaseItems)).items()) {
            boolean answer = inputRetryUtil.askForFullPricePurchase(item.name(), item.quantity());
            if (!answer) {
                return true;
            }
        }
        return false;
    }

    private List<ItemDto> applyAdditionalPromotions(List<ItemDto> purchaseItems) {
        List<ItemDto> updatedItems = new ArrayList<>(purchaseItems);

        for (ItemDto item : promotionService.findAdditionalFreeItems(new PurchaseItemsDto(purchaseItems)).products()) {
            boolean answer = inputRetryUtil.askForAdditionalPromotion(item.name(), item.quantity());
            if (answer) {
                updatedItems = updateItemQuantity(updatedItems, item.name(), item.quantity());
            }
        }
        return updatedItems;
    }

    private List<ItemDto> updateItemQuantity(List<ItemDto> itemDtos, String name, int additionalQuantity) {
        List<ItemDto> updatedItems = new ArrayList<>();

        for (ItemDto itemDto : itemDtos) {
            if (itemDto.name().equals(name)) {
                updatedItems.add(new ItemDto(name, itemDto.quantity() + additionalQuantity));
            } else if (!itemDto.name().equals(name)) {
                updatedItems.add(itemDto);
            }
        }
        return updatedItems;
    }
}
