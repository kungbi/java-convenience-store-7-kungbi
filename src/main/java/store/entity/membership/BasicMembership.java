package store.entity.membership;

import store.Configuration;
import store.exception.MembershipException;
import store.exception.message.MembershipExceptionMessage;

public class BasicMembership implements Membership {


    @Override
    public int calculateDiscountedPrice(int originalPrice) {
        if (originalPrice <= 0) {
            throw new MembershipException(MembershipExceptionMessage.INVALID_PRICE);
        }

        try {
            int discountAmount = Math.floorDiv(originalPrice * Configuration.MEMBERSHIP_DISCOUNT_RATE.getInt(), 100);
            return Math.min(Configuration.MEMBERSHIP_DISCOUNT_MAX_AMOUNT.getInt(), discountAmount);
        } catch (ArithmeticException error) {
            throw new MembershipException(MembershipExceptionMessage.DIVIDE_BY_ZERO, error);
        }
    }
}
