package store.validator;

import java.util.List;
import store.dto.ItemDto;

public class InputValidator {
    public static void purchaseItemsValidate(List<ItemDto> purchaseItems) {
        if (purchaseItems == null) {
            throw new IllegalArgumentException("올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
        }
        if (purchaseItems.isEmpty()) {
            throw new IllegalArgumentException("올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
        }
    }

    public static void yesOrNoValidate(String input) {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException("올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
        }
        if (input.length() != 1) {
            throw new IllegalArgumentException("올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
        }
        if (!input.equals("Y") && !input.equals("N")) {
            throw new IllegalArgumentException("올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
        }
    }
}
