package store.utils.parser;

import java.time.LocalDateTime;
import store.exception.DtoException;
import store.exception.message.DtoExceptionMessage;

public record PromotionFieldsDto(String name, int buy, int get, LocalDateTime startDate, LocalDateTime endDate) {
    public PromotionFieldsDto {
        if (name == null || name.isBlank()) {
            throw new DtoException(DtoExceptionMessage.ITEM_NAME_NULL_OR_BLANK);
        }
        if (buy <= 0) {
            throw new DtoException(DtoExceptionMessage.ITEM_QUANTITY_LESS_THAN_ZERO);
        }
        if (get <= 0) {
            throw new DtoException(DtoExceptionMessage.ITEM_QUANTITY_LESS_THAN_ZERO);
        }
        if (startDate == null) {
            throw new DtoException(DtoExceptionMessage.START_DATE_NULL);
        }
        if (endDate == null) {
            throw new DtoException(DtoExceptionMessage.END_DATE_NULL);
        }
    }
}
