package store.utils.parser;

import java.io.IOException;
import java.util.List;
import store.controller.InputParser;

public class ProductParser {
    private final Reader reader;

    public ProductParser(Reader reader) {
        this.reader = reader;
    }

    public ProductFieldsDto nextProduct() throws IOException {
        List<String> fields = reader.readLine();

        if (fields == null || fields.isEmpty()) {
            return null;
        }

        if (fields.size() != 4) {
            throw new IllegalArgumentException("Invalid product data");
        }

        String name = fields.get(0);
        int price = InputParser.parseInteger(fields.get(1));
        int stock = InputParser.parseInteger(fields.get(2));
        String promotionName = fields.get(3);

        if (promotionName.equals("null")) {
            return new ProductFieldsDto(name, price, stock, null);
        }

        return new ProductFieldsDto(name, price, stock, promotionName);
    }

}
