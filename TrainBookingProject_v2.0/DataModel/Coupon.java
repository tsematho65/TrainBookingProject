package DataModel;

public interface Coupon {
	double getDiscount(double totalAmount);

	String getCouponType();

	String getCouponCode();

	boolean isExpired();
    
}

