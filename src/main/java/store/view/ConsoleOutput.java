package store.view;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import store.dto.ItemDto;
import store.dto.ProductInfoDto;
import store.dto.PurchaseResultDto;
import store.dto.PurchaseResultItemDto;

public class ConsoleOutput {

    // 인사말 출력
    public void printWelcomeMessage() {
        System.out.println("안녕하세요. w편의점입니다.");
    }

    // 상품 목록을 지정된 형식으로 출력하는 메서드
    public void printProductList(List<ProductInfoDto> products) {
        System.out.println("현재 보유하고 있는 상품입니다.\n");
        for (ProductInfoDto product : products) {
            StringBuilder output = new StringBuilder();
            output.append("- ").append(product.name()).append(" ").append(String.format("%,d", product.price()))
                    .append("원 ");

            if (product.quantity() > 0) {
                output.append(product.quantity()).append("개");
            } else {
                output.append("재고 없음");
            }

            if (product.promotion() != null && !product.promotion().isEmpty()) {
                output.append(" ").append(product.promotion());
            }

            System.out.println(output.toString());
        }
    }

    // 구매 요약 출력
    public void printPurchaseSummary(PurchaseResultDto purchaseResult) {
        NumberFormat currencyFormat = NumberFormat.getInstance(Locale.KOREA);

        System.out.println("==============W 편의점================");
        System.out.printf("%-10s %10s %10s\n", "상품명", "수량", "금액");

        for (PurchaseResultItemDto item : purchaseResult.purchaseItems()) {
            String itemName = item.name();
            int quantity = item.quantity();
            int itemPrice = item.price();
            System.out.printf("%-10s %10d %15s\n", itemName, quantity, itemPrice);
        }

        System.out.println("===============증 정=================");
        for (ItemDto item : purchaseResult.freeItems()) {
            System.out.printf("%-10s %10d\n", item.name(), item.quantity());
        }

        System.out.println("====================================");
        System.out.printf("%-10s %25s\n", "총구매액", currencyFormat.format(purchaseResult.totalAmount()));
        System.out.printf("%-10s %25s\n", "행사할인",
                currencyFormat.format(purchaseResult.promotionDiscountAmount() * -1));
        System.out.printf("%-10s %25s\n", "멤버십할인",
                currencyFormat.format(purchaseResult.membershipDiscountAmount() * -1));
        System.out.printf("%-10s %25s\n", "내실돈", currencyFormat.format(purchaseResult.paymentAmount()));
    }

    public void printException(Exception e) {
        System.out.println("[ERROR] " + e.getMessage());
    }

}
