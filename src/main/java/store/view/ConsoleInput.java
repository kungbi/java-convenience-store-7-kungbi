package store.view;

import camp.nextstep.edu.missionutils.Console;

public class ConsoleInput {

    public String getPurchaseItems() {
        newLine();
        return readUserInput("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
    }

    public String askForAdditionalPromotion(String productName, int quantity) {
        newLine();
        return readUserInput("현재 " + productName + "은(는) " + quantity + "개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)");
    }

    public String askForFullPricePurchase(String productName, int quantity) {
        newLine();
        return readUserInput("현재 " + productName + " " + quantity + "개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)");
    }

    public String askForMembershipDiscount() {
        newLine();
        return readUserInput("멤버십 할인을 받으시겠습니까? (Y/N)");
    }

    public String askForAdditionalPurchase() {
        newLine();
        return readUserInput("감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)");
    }

    private void newLine() {
        System.out.println();
    }

    private String readUserInput(String hint) {
        System.out.println(hint);
        return Console.readLine();
    }
}
