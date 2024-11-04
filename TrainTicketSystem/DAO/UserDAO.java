package DAO;
import java.util.*;

import DB_init.*;
import DataModel.*;

public class UserDAO {
    private ArrayList<User> table_user;
    private User nowUser;
    
    public UserDAO() {
        table_user = Database.getInstance().getTable_user();
    }
    

    public ArrayList<User> print_user(User user) {
    	
        return user.toString();
        
        
    }

	public String getNowUser() {
		return nowUser.toString();
	}
	
	public User getNowUser_fromUserTable() {
	}
		return nowUser;
	}

    
    public boolean login(String userName, String pwd) {
       
        for (User user : table_user) {
            if (user.getUsername().equals(userName) && user.getPassword().equals(pwd)) {
            	this.nowUser = user;
                return true;
            }
        }
        
        return false;
    }
    
    public double getDiscount() {
    	return nowUser.getMember().getDiscount();
    }
    
	public double getDiscount(String userName) {
		for (User user : table_user) {
			if (user.getUsername().equals(userName)) {
				return user.getMember().getDiscount();
			}
		}
		return 0;
	}
	

	public String register(String userName, String pwd) {
		for (User user : table_user) {
			if (user.getUsername().equals(userName)) {
				return "User already exists!";
			}
		}
		table_user.add(new User("normal", "userID_" + (table_user.size() + 1), userName, pwd));
		return "Register successfully!";
	}

	
	
    public ArrayList<User> getUserList(List<User> userList) {
    	        return table_user;
    }
    
	public void printUserList() {
		for (User user : table_user) {
			System.out.println(print_user(user));
		}
	}

    public boolean addUser_fromUserTable(User user) {
        table_user.add(user);
        return true;
    }

	public boolean updateUser_fromUserTable(User user) {
		boolean result = false;
		User foundUser = getUser_fromUserTable(user.getId());

		if (foundUser != null) {
			foundUser.setUsername(user.getUsername());
			foundUser.setPassword(user.getPassword());
			foundUser.setRole(user.getRole());
			result = true;
		}
		return result;
	}

	public boolean deleteUser_fromUserTable(String userName) {
		boolean result = false;
		User foundUser = getUser_fromUserTable(userName);

		if (foundUser != null) {
			table_user.remove(foundUser);
			result = true;
		}
		return result;
	}
}
