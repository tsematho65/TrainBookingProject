package AccountFactory;

public class AccountFactory {
    public Account createUser(String username, String password, String email, String phone){
        return new UserAccount(username, password, email, phone);
    }
    public Account createAdmin(String username, String password, String email, String phone){
        return new AdminAccount(username, password, email, phone);
    }
}