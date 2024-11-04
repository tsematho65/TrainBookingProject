package DataModel;

public class PlatinumMember implements MembershipState {
    
    public String getMemberType() {
        return "Platinum Member";
    }
    
	public double getDiscount() {
		return 0.2;
	}
}
