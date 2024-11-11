package store.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import store.dto.ItemDto;
import store.exception.InputParserException;
import store.exception.message.InputParserExceptionMessage;

public class InputParser {
    public static List<ItemDto> parseItems(String input) {
        validate(input);
        List<ItemDto> purchaseItems = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\[(.+)-(.+)\\]");
        for (String token : input.split(",")) {
            Matcher matcher = pattern.matcher(token);
            while (matcher.find()) {
                String name = matcher.group(1).strip();     // 상품명
                int quantity = parseInteger(matcher.group(2).strip()); // 수량
                purchaseItems.add(new ItemDto(name, quantity));
            }
        }
        return purchaseItems;
    }

    public static int parseInteger(String input) {
        validate(input);
        return parseSingleInteger(input);
    }

    // private methods

    private static void validate(String input) {
        if (input == null || input.isBlank()) {
            throw new InputParserException(InputParserExceptionMessage.NULL_OR_BLANK);
        }
    }

    private static int parseSingleInteger(String input) {
        try {
            return Integer.parseInt(input.strip());
        } catch (NumberFormatException error) {
            throw new InputParserException(InputParserExceptionMessage.NUMBER_FORMAT_OR_OVERFLOW_EXCEPTION);
        }
    }
}
