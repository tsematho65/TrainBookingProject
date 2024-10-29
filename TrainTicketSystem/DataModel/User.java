// User.java
package DataModel;

public class User {
    private String id;
    private String username;
    private String password;
    private String role;
    private String status;

    public User(String role, String id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.status = "active";
    }

   
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public void setRole(String role) {
        this.role = role;
    }
    public String getRole() {
        return role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}