package store.utils.Date;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.DateTimeException;
import java.time.LocalDateTime;

public class LocalDateTimes {
    public static LocalDateTime of(String stringDate) {
        if (stringDate == null) {
            throw new DateTimeException("NULL 입력이 있습니다.");
        }

        if (stringDate.contains("T")) {
            stringDate = stringDate.split("T")[0];
        }

        String[] tokens = stringDate.split("-");
        if (tokens.length != 3) {
            throw new DateTimeException("날짜 형식이 잘못되었습니다.");
        }

        try {
            int year = Integer.parseInt(tokens[0]);
            int month = Integer.parseInt(tokens[1]);
            int day = Integer.parseInt(tokens[2]);
            return DateTimes.now().withYear(year).withMonth(month).withDayOfMonth(day);
        } catch (NumberFormatException error) {
            throw new DateTimeException("날짜 형식이 잘못되었습니다.", error);
        }

    }

    public static LocalDateTime now() {
        return DateTimes.now();
    }

}
