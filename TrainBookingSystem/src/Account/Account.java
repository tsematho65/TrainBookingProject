package Account;

public class Account {
	//user information to make account management and login
	private String username;
	private String password;
	private String email;
	private String phone;
	private String type;

	
	//constructor
	public Account(String username, String password, String email, String phone, String type) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.phone = phone;
		this.type = type;
	}
	
	//getters
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
	
	//setters
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
}
