package store.utils.parser;

import java.io.IOException;
import java.util.List;
import store.controller.InputParser;
import store.utils.Date.LocalDateTimes;

public class PromotionParser {
    private final Reader reader;

    public PromotionParser(Reader reader) {
        this.reader = reader;
    }

    public PromotionFieldsDto nextPromotion() throws IOException {
        List<String> fields = reader.readLine();

        if (fields == null || fields.isEmpty()) {
            return null;
        }

        return new PromotionFieldsDto(fields.get(0), InputParser.parseInteger(fields.get(1)),
                InputParser.parseInteger(fields.get(2)),
                LocalDateTimes.of(fields.get(3)), LocalDateTimes.of(fields.get(4)));
    }
}
