package DAO;

import DataModel.*;
import java.time.LocalDate;
import java.util.Random;

public class DaliyCheckInDAO {
    private static final String[] COUPON_TYPES = {"Amount", "Discount"};
    private static final String COUPON_CODE_PREFIX = "COUPON";

    public DaliyCheckInDAO() {
    }

    public void checkIn(User currentUser) {
        LocalDate today = LocalDate.now();

       
        if (currentUser.getLastSignInDate() != null && currentUser.getLastSignInDate().equals(today)) {
            System.out.println("You have already checked in today.");
            return;
        }

       
        currentUser.setLastSignInDate(today);

        
        int pointsEarned = 10; // 
        currentUser.setPoints(currentUser.getPoints() + pointsEarned);
        System.out.println(currentUser.getUsername() + " successful CheckIn " + pointsEarned + " Points Earned!");

        
        if (generateRandomCoupon(currentUser)) {
            System.out.println("Congratulations! You have received a coupon.");
        } else {
            System.out.println("Sorry, you did not receive a coupon this time.");
        }
    }

    public static boolean generateRandomCoupon(User currentUser) {
        Random random = new Random();
        String couponType = COUPON_TYPES[random.nextInt(COUPON_TYPES.length)];
        String couponCode = COUPON_CODE_PREFIX + random.nextInt(10000);
        LocalDate expiryDate = LocalDate.now().plusDays(7);
        double discount=0;
        CouponFactory cuponFactory;
        boolean generateCoupon = random.nextBoolean();
        if(generateCoupon) {

	        if (couponType.equals("Amount")) {
	            discount = 10 + (50 - 10) * random.nextDouble();
	           cuponFactory = new AmountCouponFactory();
	            
	        } else {
	            discount = 0.01 + (0.2 - 0.05) * random.nextDouble();
	            cuponFactory = new DiscountCouponFactory();
	        }
	        currentUser.addCoupon(cuponFactory.createCoupon(discount, couponCode, expiryDate));
        }
        
        return generateCoupon;
    }
}