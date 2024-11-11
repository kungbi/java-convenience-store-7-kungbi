package store.exception.message;

public enum ReaderExceptionMessage implements ExceptionMessage {
    BUFFERED_READER_NULL("BufferedReader 는 null 일 수 없습니다."),
    READ_LINE_FAILED("라인을 읽는데 실패했습니다."),

    ;

    private final String message;

    ReaderExceptionMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
