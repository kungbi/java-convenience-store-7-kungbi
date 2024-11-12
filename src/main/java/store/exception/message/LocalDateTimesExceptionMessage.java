package store.exception.message;

public enum LocalDateTimesExceptionMessage implements ExceptionMessage {
    DATE_NULL("날짜가 NULL 입니다."),
    DATE_FORMAT_ERROR("날짜 형식이 잘못되었습니다.");

    private final String message;


    LocalDateTimesExceptionMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
