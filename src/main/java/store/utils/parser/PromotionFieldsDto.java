package store.utils.parser;

import java.time.LocalDateTime;
import store.exception.DtoException;
import store.exception.message.DtoExceptionMessage;

public record PromotionFieldsDto(String name, int buy, int get, LocalDateTime startDate, LocalDateTime endDate) {

    public PromotionFieldsDto {
        validateName(name);
        validateQuantity(buy, DtoExceptionMessage.ITEM_QUANTITY_LESS_THAN_ZERO);
        validateQuantity(get, DtoExceptionMessage.ITEM_QUANTITY_LESS_THAN_ZERO);
        validateDate(startDate, DtoExceptionMessage.START_DATE_NULL);
        validateDate(endDate, DtoExceptionMessage.END_DATE_NULL);
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new DtoException(DtoExceptionMessage.ITEM_NAME_NULL_OR_BLANK);
        }
    }

    private void validateQuantity(int quantity, DtoExceptionMessage message) {
        if (quantity <= 0) {
            throw new DtoException(message);
        }
    }

    private void validateDate(LocalDateTime date, DtoExceptionMessage message) {
        if (date == null) {
            throw new DtoException(message);
        }
    }
}
