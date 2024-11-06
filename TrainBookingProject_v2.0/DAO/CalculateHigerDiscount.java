
package DAO;
import DataModel.*;

import java.util.*;

public class CalculateHigerDiscount {

    public double calculateDiscount(double totalAmount, User currentUser) {

        ArrayList<Coupon> coupons = currentUser.getCouponList();
        double maxDiscount = 0.0;
        for (Coupon coupon : coupons) {
            if (coupon.getDiscount(totalAmount) > maxDiscount) {
                maxDiscount = coupon.getDiscount(totalAmount);
            }
        }
        return maxDiscount;
    }
}
