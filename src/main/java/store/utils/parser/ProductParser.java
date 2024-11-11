package store.utils.parser;

import java.io.IOException;
import java.util.List;
import store.controller.InputParser;

public class ProductParser {
    public static final String NULL_STRING = "null";
    private final Reader reader;

    public ProductParser(Reader reader) {
        this.reader = reader;
    }

    public ProductFieldsDto nextProduct() throws IOException {
        List<String> fields = reader.readLine();
        if (fields == null || fields.isEmpty()) {
            return null;
        }
        validate(fields);
        return new ProductFieldsDto(
                fields.get(0),
                InputParser.parseInteger(fields.get(1)), InputParser.parseInteger(fields.get(2)),
                parsePromotionName(fields.get(3)));
    }

    private String parsePromotionName(String promotionName) {
        if (promotionName.equals(NULL_STRING)) {
            return null;
        }
        return promotionName;
    }

    private static void validate(List<String> fields) {
        if (fields.size() != 4) {
            throw new IllegalArgumentException(ParserExceptionMessage.INVALID_PROMOTION_DATA.getMessage());
        }
    }
}
