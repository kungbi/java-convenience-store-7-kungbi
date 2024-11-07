package store.exception.message;

public enum MembershipExceptionMessage implements ExceptionMessage {
    DIVIDE_BY_ZERO("0으로 나눌 수 없습니다."),
    INVALID_PRICE("가격은 0보다 커야 합니다.");

    private final String message;

    MembershipExceptionMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
