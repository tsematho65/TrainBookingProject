package DataModel;

import java.time.LocalDate;

public interface CouponFactory {
    Coupon createCoupon(double value, String couponCode, LocalDate expiryDate);
}