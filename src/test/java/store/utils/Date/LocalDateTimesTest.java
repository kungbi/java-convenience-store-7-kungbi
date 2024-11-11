package store.utils.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.DateTimeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LocalDateTimesTest {

    @Test
    void 정상__날짜를_객체로_변환() {
        // given
        String stringDate = "2021-10-10";

        // when
        var result = LocalDateTimes.of(stringDate);

        // then
        assertEquals(2021, result.getYear());
        assertEquals(10, result.getMonthValue());
        assertEquals(10, result.getDayOfMonth());
    }

    @Test
    void 정상__날짜를_객체로_변환_시간이_포함된_문자열_경우() {
        // given
        String stringDate = "2023-10-12T15:43:21.123456789";

        // when
        var result = LocalDateTimes.of(stringDate);

        // then
        assertEquals(2023, result.getYear());
        assertEquals(10, result.getMonthValue());
        assertEquals(12, result.getDayOfMonth());
    }

    @Test
    void 예외__날짜를_객체로_변환_날짜가_없는_경우() {
        // given
        String stringDate = "2021-10";

        // when
        DateTimeException exception = Assertions.assertThrows(DateTimeException.class,
                () -> LocalDateTimes.of(stringDate));

        // then
        assertEquals("날짜 형식이 잘못되었습니다.", exception.getMessage());
    }

    @Test
    void 예외__날짜를_객체로_변환_NULL_입력() {
        // given
        String stringDate = null;

        // when
        DateTimeException exception = Assertions.assertThrows(DatesT,
                () -> LocalDateTimes.of(stringDate));

        // then
        assertEquals("NULL 입력이 있습니다.", exception.getMessage());
    }

}