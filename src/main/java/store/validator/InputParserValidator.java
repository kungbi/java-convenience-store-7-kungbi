package store.validator;

import java.util.List;
import store.dto.ItemDto;
import store.exception.InputParserException;
import store.exception.message.InputParserExceptionMessage;

public class InputParserValidator {
    public static void purchaseItemsValidate(List<ItemDto> purchaseItems) {
        if (purchaseItems == null || purchaseItems.isEmpty()) {
            throw new InputParserException(InputParserExceptionMessage.PURCHASE_ITEMS_NULL_OR_EMPTY);
        }
    }

    public static void yesOrNoValidate(String input) {
        if (input == null || input.isBlank()) {
            throw new InputParserException(InputParserExceptionMessage.INVALID_FORMAT);
        }
        if (input.length() != 1) {
            throw new InputParserException(InputParserExceptionMessage.INVALID_FORMAT);
        }
        if (!input.equals("Y") && !input.equals("N")) {
            throw new InputParserException(InputParserExceptionMessage.INVALID_FORMAT);
        }
    }
}
