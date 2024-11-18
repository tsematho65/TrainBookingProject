
package DataModel;

import java.time.LocalDate;


public class DiscountCouponFactory implements CouponFactory {
    @Override
    public Coupon createCoupon(double value, String couponCode, LocalDate expiryDate) {
        return new DiscountCoupon(value, "DiscountCoupon", couponCode, expiryDate);
    }
}