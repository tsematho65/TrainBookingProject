package AccountFactory;

public class UserAccount extends Account {
    public UserAccount(String username, String password, String email, String phone) {
        super(username, password, email, phone, phone);
        this.setType("user");
    }

    // Additional user-specific methods
}