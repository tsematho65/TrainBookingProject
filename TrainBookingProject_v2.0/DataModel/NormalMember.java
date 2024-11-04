package DataModel;

public class NormalMember implements MembershipState {
    
    public String getMemberType() {
        return "Normal Member";
    }
    
	public double getDiscount() {
		return 0.1;
	}
}
