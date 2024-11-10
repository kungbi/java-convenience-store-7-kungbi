package store.view;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.dto.ItemDto;
import store.dto.ProductInfoDto;
import store.dto.PurchaseResultDto;
import store.dto.PurchaseResultItemDto;

public class ConsoleOutputTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private ConsoleOutput consoleOutput;

    @BeforeEach
    public void setUp() {
//        System.setOut(new PrintStream(outContent));
        consoleOutput = new ConsoleOutput();
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    public void testPrintWelcomeMessage() {
        consoleOutput.printWelcomeMessage();
        Assertions.assertEquals("안녕하세요. 웅비편의점입니다.\n", outContent.toString());
    }

    @Test
    public void testPrintProductList() {
        List<ProductInfoDto> products = List.of(
                new ProductInfoDto("콜라", 1000, 7, "탄산2+1"),
                new ProductInfoDto("사이다", 1000, 0, "탄산2+1"),
                new ProductInfoDto("물", 500, 10, null)
        );
        consoleOutput.printProductList(products);

        String expectedOutput = "현재 보유하고 있는 상품입니다.\n\n" +
                                "- 콜라 1000원 7개 탄산2+1\n" +
                                "- 사이다 1000원 재고 없음 탄산2+1\n" +
                                "- 물 500원 10개\n\n";

        Assertions.assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void testPrintPurchaseSummary() {
        List<PurchaseResultItemDto> purchasedItems = List.of(
                new PurchaseResultItemDto("콜라", 3, 3000),
                new PurchaseResultItemDto("사이다", 2, 2000)
        );
        List<ItemDto> freeItems = List.of(new ItemDto("콜라", 1));
        int totalAmount = 13000;
        int promotionDiscount = 1000;
        int membershipDiscount = 3000;
        int finalAmount = 9000;

        consoleOutput.printPurchaseSummary(
                new PurchaseResultDto(purchasedItems, freeItems, totalAmount, promotionDiscount, membershipDiscount, finalAmount)
        );

        String expectedOutput = "==============W 편의점================\n" +
                                "상품명\t\t수량\t금액\n" +
                                "콜라\t\t3\t3000\n" +
                                "사이다\t\t2\t2000\n" +
                                "=============증정===============\n" +
                                "콜라\t\t1\n" +
                                "====================================\n" +
                                "총구매액\t\t13000\n" +
                                "행사할인\t\t-1000\n" +
                                "멤버십할인\t\t-3000\n" +
                                "내실돈\t\t9000\n";

        Assertions.assertEquals(expectedOutput, outContent.toString());
    }

}
