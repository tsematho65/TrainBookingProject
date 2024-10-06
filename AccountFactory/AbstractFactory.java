package AccountFactory;

public abstract class AbstractFactory {
    public abstract Account createUser(String username, String password, String email, String phone);
    public abstract Account createAdmin(String username, String password, String email, String phone);
}