package store.utils.parsor;

import java.io.IOException;
import java.util.List;
import store.utils.Date.LocalDateTimes;

public class PromotionParser {
    private final Reader reader;

    public PromotionParser(Reader reader) {
        this.reader = reader;
    }

    public PromotionFieldsDto nextProduct() throws IOException {
        List<String> fields = reader.readLine();

        if (fields == null || fields.isEmpty()) {
            return null;
        }

        return new PromotionFieldsDto(fields.get(0), getParseInt(fields.get(1)), getParseInt(fields.get(2)),
                LocalDateTimes.of(fields.get(3)), LocalDateTimes.of(fields.get(4)));
    }

    private int getParseInt(String field) {
        try {
            return Integer.parseInt(field);
        } catch (NumberFormatException error) {
            throw new IllegalArgumentException("Invalid promotion data", error);
        }
    }
}
