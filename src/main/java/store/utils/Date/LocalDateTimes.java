package store.utils.Date;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDateTime;
import store.exception.LocalDateTimesException;
import store.exception.message.LocalDatesTimeExceptionMessage;

public class LocalDateTimes {
    public static LocalDateTime of(String stringDate) {
        if (stringDate == null) {
            throw new LocalDateTimesException(LocalDatesTimeExceptionMessage.DATE_NULL);
        }

        if (stringDate.contains("T")) {
            stringDate = stringDate.split("T")[0];
        }

        String[] tokens = stringDate.split("-");
        if (tokens.length != 3) {
            throw new LocalDateTimesException(LocalDatesTimeExceptionMessage.DATE_FORMAT_ERROR);
        }

        try {
            int year = Integer.parseInt(tokens[0]);
            int month = Integer.parseInt(tokens[1]);
            int day = Integer.parseInt(tokens[2]);
            return DateTimes.now().withYear(year).withMonth(month).withDayOfMonth(day);
        } catch (NumberFormatException error) {
            throw new LocalDateTimesException(LocalDatesTimeExceptionMessage.DATE_FORMAT_ERROR, error);
        }

    }

    public static LocalDateTime now() {
        return DateTimes.now();
    }

}
