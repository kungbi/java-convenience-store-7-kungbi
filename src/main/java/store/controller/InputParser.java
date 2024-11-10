package store.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import store.dto.ItemDto;

public class InputParser {
    public static List<ItemDto> parseItems(String input) {
        validate(input);
        List<ItemDto> purchaseItems = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\[(.+)-(\\d+)\\]");
        for (String token : input.split(",")) {
            Matcher matcher = pattern.matcher(token);
            while (matcher.find()) {
                String name = matcher.group(1);     // 상품명
                int quantity = Integer.parseInt(matcher.group(2)); // 수량
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
            throw new IllegalArgumentException("최소 한 개 이상의 숫자를 입력해주세요.");
        }
    }

    private static int parseSingleInteger(String input) {
        try {
            return Integer.parseInt(input.strip());
        } catch (NumberFormatException error) {
            throw new IllegalArgumentException("숫자만 입력해주세요.");
        }
    }
}
