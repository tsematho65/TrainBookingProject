package DataModel;

public class NormalMember implements Member {
    
    public String getMemberType() {
        return "Normal Member";
    }
    
	public double getDiscount() {
		return 0.1;
	}
}
