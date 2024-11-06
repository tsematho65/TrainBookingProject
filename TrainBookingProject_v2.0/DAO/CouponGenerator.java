package DAO;
import DataModel.*;
import java.time.LocalDate;
import java.util.Random;

public class CouponGenerator {

    private static final String[] COUPON_TYPES = {"Amount", "Discount"};
    private static final String COUPON_CODE_PREFIX = "COUPON";

    public static boolean generateRandomCoupon(User currentUser) {
        Random random = new Random();
        String couponType = COUPON_TYPES[random.nextInt(COUPON_TYPES.length)];
        String couponCode = COUPON_CODE_PREFIX + random.nextInt(10000);
        LocalDate expiryDate = LocalDate.now().plusDays(7);

        if (couponType.equals("Amount")) {
            double amount = 10 + (50 - 10) * random.nextDouble(); 
            currentUser.addCoupon( new AmountCoupon(amount, couponType, couponCode, expiryDate));
        } else {
            double discount = 0.01 + (0.2 - 0.05) * random.nextDouble(); 
            currentUser.addCoupon(new DiscountCoupon(discount, couponType, couponCode, expiryDate));
        }
        
        return true;
    }

}
