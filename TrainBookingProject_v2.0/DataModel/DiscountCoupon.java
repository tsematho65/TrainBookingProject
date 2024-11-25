
package DataModel;

import java.time.LocalDate;

public class DiscountCoupon implements Coupon {
    private double discount;
    private String couponType;
    private String couponCode;
    private LocalDate expiryDate;

    public DiscountCoupon(double discount, String couponType, String couponCode, LocalDate expiryDate) {
        this.discount = discount;
        this.couponType = "DiscountCoupon";
        this.couponCode = couponCode;
        this.expiryDate = expiryDate;
    }


    public double getDiscount(double totalAmount) {
        
        return totalAmount * discount;
    }


}
