
package DataModel;

import java.time.LocalDate;

public class DiscountCoupon implements Coupon {
    private double discount;
    private String couponType;
    private String couponCode;
    private LocalDate expiryDate;

    public DiscountCoupon(double discount, String couponType, String couponCode, LocalDate expiryDate) {
<<<<<<< HEAD
        // this.couponId = couponId;
=======
>>>>>>> branch 'main' of https://github.com/JayV0128/TrainBookingProject.git
        this.discount = discount;
        this.couponType = "DiscountCoupon";
        this.couponCode = couponCode;
        this.expiryDate = expiryDate;
    }


    public double getDiscount(double totalAmount) {
        
        return totalAmount * discount;
    }

    public String getCouponType() {
        return couponType;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public boolean isExpired() {
        return LocalDate.now().isAfter(expiryDate);
    }
}
