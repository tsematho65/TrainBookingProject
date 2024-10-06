package AccountFactory;

public class AdminAccount extends Account {
    public AdminAccount(String username, String password, String email, String phone) {
        super(username, password, email, phone, phone);
        this.setType("admin");
    }
    // Additional admin-specific methods
}
