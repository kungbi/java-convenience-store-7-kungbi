package store.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import store.dto.ItemDto;

public class InputParser {
    public static List<ItemDto> parseItems(String input) {
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
}
