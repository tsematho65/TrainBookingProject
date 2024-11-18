package DataModel;

import java.time.LocalDate;


public class AmountCouponFactory implements CouponFactory {
    @Override
    public Coupon createCoupon(double value, String couponCode, LocalDate expiryDate) {
        return new AmountCoupon(value, "Amount Coupon", couponCode, expiryDate);
    }
}