package store.utils.Date;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDateTime;
import store.exception.LocalDateTimesException;
import store.exception.message.LocalDateTimesExceptionMessage;

public class LocalDateTimes {

    // public methods

    public static LocalDateTime of(String stringDate) {
        validateStringDate(stringDate);

        String sanitizedDate = sanitizeDate(stringDate);
        String[] tokens = parseDateTokens(sanitizedDate);

        return createLocalDateTime(tokens);
    }

    public static LocalDateTime now() {
        return DateTimes.now();
    }

    // private methods

    private static void validateStringDate(String stringDate) {
        if (stringDate == null) {
            throw new LocalDateTimesException(LocalDateTimesExceptionMessage.DATE_NULL);
        }
    }

    private static String sanitizeDate(String stringDate) {
        if (stringDate.contains("T")) {
            return stringDate.split("T")[0];
        }
        return stringDate;
    }

    private static String[] parseDateTokens(String sanitizedDate) {
        String[] tokens = sanitizedDate.split("-");
        if (tokens.length != 3) {
            throw new LocalDateTimesException(LocalDateTimesExceptionMessage.DATE_FORMAT_ERROR);
        }
        return tokens;
    }

    private static LocalDateTime createLocalDateTime(String[] tokens) {
        try {
            int year = Integer.parseInt(tokens[0]);
            int month = Integer.parseInt(tokens[1]);
            int day = Integer.parseInt(tokens[2]);
            return DateTimes.now().withYear(year).withMonth(month).withDayOfMonth(day);
        } catch (NumberFormatException error) {
            throw new LocalDateTimesException(LocalDateTimesExceptionMessage.DATE_FORMAT_ERROR, error);
        }
    }
}
