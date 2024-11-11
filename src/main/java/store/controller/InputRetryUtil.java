package store.controller;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import store.dto.ItemDto;
import store.dto.PurchaseItemsDto;
import store.service.ProductStockService;
import store.validator.InputParserValidator;
import store.view.ConsoleInput;
import store.view.ConsoleOutput;

public class InputRetryUtil {
    final private ConsoleInput consoleInput;
    final private ConsoleOutput consoleOutput;
    final private ProductStockService productStockService;

    // constructor for dependency injection

    public InputRetryUtil(ConsoleInput consoleInput, ConsoleOutput consoleOutput,
                          ProductStockService productStockService) {
        this.consoleInput = consoleInput;
        this.consoleOutput = consoleOutput;
        this.productStockService = productStockService;
    }

    // public methods

    public List<ItemDto> getPurchaseItems() {
        return readValidatedInput(consoleInput::getPurchaseItems, InputParser::parseItems,
                (items) -> {
                    InputParserValidator.purchaseItemsValidate(items);
                    productStockService.validateStocks(new PurchaseItemsDto(
                            items
                    ));
                });
    }

    public String askForAdditionalPromotion(String productName) {
        return readValidatedInput(() -> consoleInput.askForAdditionalPromotion(productName), (context) -> context,
                InputParserValidator::yesOrNoValidate);
    }

    public String askForFullPricePurchase(String productName, int quantity) {
        return readValidatedInput(() -> consoleInput.askForFullPricePurchase(productName, quantity),
                (context) -> context, InputParserValidator::yesOrNoValidate);
    }

    public String askForMembershipDiscount() {
        return readValidatedInput(consoleInput::askForMembershipDiscount, (context) -> context,
                InputParserValidator::yesOrNoValidate);
    }

    public String askForAdditionalPurchase() {
        return readValidatedInput(consoleInput::askForAdditionalPurchase, (context) -> context,
                InputParserValidator::yesOrNoValidate);
    }

    // private methods

    private <T> T readValidatedInput(Supplier<String> inputSupplier, Function<String, T> parser,
                                     Consumer<T> validator) {
        while (true) {
            try {
                String userInput = inputSupplier.get();
                T pasredInput = parser.apply(userInput);
                validator.accept(pasredInput);
                return pasredInput;
            } catch (IllegalArgumentException error) {
                consoleOutput.printException(error);
            }
        }
    }
}
