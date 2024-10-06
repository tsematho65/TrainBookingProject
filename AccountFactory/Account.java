package AccountFactory;

public abstract class Account {
    // User information to make account management and login
    private String username;
    private String password;
    private String email;
    private String phone;
    private String type;

    // Constructor
    public Account(String username, String password, String email, String phone, String type) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
    }

    // Getters
    public String getUsername() {
        return username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getPhone() {
        return phone;
    }

    public String getType() {
        return type;
    }

    // Setters
    public void setUsername(String name) {
        name = username;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public void setEmail(String  email) {
        this.email = email;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setType(String type) {
        this.type = type;
    }
}