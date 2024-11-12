package store.view;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import store.dto.ItemDto;
import store.dto.ProductInfoDto;
import store.dto.PurchaseResultDto;
import store.dto.PurchaseResultItemDto;

public class ConsoleOutput {

    private final NumberFormat currencyFormat = NumberFormat.getInstance(Locale.KOREA);

    // public methods

    public void printWelcomeMessage() {
        System.out.println("안녕하세요. W편의점입니다.");
    }

    public void printProductList(List<ProductInfoDto> products) {
        System.out.println("현재 보유하고 있는 상품입니다.\n");
        for (ProductInfoDto product : products) {
            printProductInfo(product);
        }
    }

    public void printPurchaseSummary(PurchaseResultDto purchaseResult) {
        System.out.println("===============W 편의점=================");
        System.out.printf("%-10s %10s %12s\n", "상품명", "수량", "금액");
        printPurchaseItems(purchaseResult.purchaseItems());
        printFreeItems(purchaseResult.freeItems());
        System.out.println("======================================");
        printSummary(purchaseResult);
    }

    public void printException(Exception e) {
        System.out.println("[ERROR] " + e.getMessage());
    }

    // private methods

    private void printProductInfo(ProductInfoDto product) {
        String name = product.name();
        int price = product.price();
        String quantity = getQuantityString(product.quantity());
        String promotion = getPromotionString(product.promotion());

        System.out.printf("- %s %,d원 %s %s\n", name, price, quantity, promotion);
    }

    private String getQuantityString(int quantity) {
        if (quantity > 0) {
            return quantity + "개";
        }
        return "재고 없음";
    }

    private String getPromotionString(String promotion) {
        if (promotion != null && !promotion.isEmpty()) {
            return promotion;
        }
        return "";
    }

    private void printPurchaseItems(List<PurchaseResultItemDto> items) {
        for (PurchaseResultItemDto item : items) {
            String name = item.name();
            int quantity = item.quantity();
            String price = formatCurrency(item.price());
            System.out.printf("%-10s %10d %15s\n", name, quantity, price);
        }
    }

    private void printFreeItems(List<ItemDto> freeItems) {
        if (freeItems.isEmpty()) {
            return;
        }
        System.out.println("================증 정==================");
        for (ItemDto item : freeItems) {
            System.out.printf("%-10s %10d\n", item.name(), item.quantity());
        }
    }

    private void printSummary(PurchaseResultDto result) {
        printSummaryLine("총구매액", result.totalAmount());
        printSummaryLine("행사할인", -result.promotionDiscountAmount());
        printSummaryLine("멤버십할인", -result.membershipDiscountAmount());
        printSummaryLine("내실돈", result.paymentAmount());

    }

    private void printSummaryLine(String label, int amount) {
        System.out.printf("%-10s %25s\n", label, formatCurrency(amount));
    }

    private String formatCurrency(int amount) {
        return currencyFormat.format(amount);
    }
}
