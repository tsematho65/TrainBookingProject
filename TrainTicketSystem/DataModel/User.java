// User.java
package DataModel;

public class User {
    private String id;
    private String username;
    private String password;
    private String role;
    private Member member;
    private int points; 
    private LocalDate lastSignInDate; 
    

    public User(String role, String id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.member = new NormalMember();
        lastSignInDate = null;
        points = 0; 
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

	public void setMember(Member member) {
		this.member = member;
	}

	public String getMemberType() {
		return member.getMemberType();
	}

	public double getDiscount() {
		return member.getDiscount();
	}
	
	
	public boolean signIn() {
        LocalDate today = LocalDate.now();
        if (lastSignInDate != null && lastSignInDate.equals(today)) {
            System.out.println("You have already signed in today. Please come back tomorrow.");
            return false; 
        }
        

        lastSignInDate = today;
        
        points += POINTS_PER_SIGN_IN;
        
     
        if (isWinningSignIn()) {
            System.out.println(username + " signIn sucessful, you get " + POINTS_PER_SIGN_IN + " points and win a prize!");
        } else {
            System.out.println(username + " signIn sucessful, you get " + POINTS_PER_SIGN_IN + "points but no prize. ");
        }
        
        return true; 
    }

    private boolean isWinningSignIn() {
        
        Random random = new Random();
        return random.nextInt(10) < 1; 
    }
}
}