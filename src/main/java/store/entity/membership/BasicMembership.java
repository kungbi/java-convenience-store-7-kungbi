package store.entity.membership;

import store.exception.MembershipException;
import store.exception.message.MembershipExceptionMessage;

public class BasicMembership implements Membership {
    private static final int DISCOUNT_RATE = 30;
    private static final int MAZ_DISCOUNT_AMOUNT = 8_000;


    @Override
    public int applyDiscount(int originalPrice) {
        if (originalPrice <= 0) {
            throw new MembershipException(MembershipExceptionMessage.INVALID_PRICE);
        }

        try {
            int discountAmount = Math.floorDiv(originalPrice * DISCOUNT_RATE, 100);
            return originalPrice - Math.min(discountAmount, MAZ_DISCOUNT_AMOUNT);
        } catch (ArithmeticException error) {
            throw new MembershipException(MembershipExceptionMessage.DIVIDE_BY_ZERO, error);
        }
    }
}
