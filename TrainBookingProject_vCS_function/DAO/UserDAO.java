package DAO;
import java.util.*;

import DB_init.*;
import DataModel.*;

public class UserDAO {
    private ArrayList<User> table_user;
    
    public UserDAO() {
        table_user = Database.getInstance().getTable_user();
    }

    public ArrayList<User> getTable_user() {
        return table_user;
    }

    public User user_login(String userName, String pwd) {
        User targetUser = null;
        for (User user : table_user) {
            if (user.getUsername().equals(userName) && user.getPassword().equals(pwd)) {
                targetUser = user;
            }
        }
        
        return targetUser;
    }

    public User getUser_fromUserTable(String id) {
        User targetUser = null;
        for (User user : table_user) {
            if (user.getId().equals(id)) {
                targetUser = user;
            }
        }
        
        return targetUser;
    }

    public User getUser_fromUserTable(String uname, String pwd) {
        User targetUser = null;
        for (User user : table_user) {
            if (user.getUsername().equals(uname) && user.getPassword().equals(pwd)) {
                targetUser = user;
            }
        }
        
        return targetUser;
    }

    public boolean addUser_fromUserTable(User user) {
        table_user.add(user);
        return true;
    }

    public boolean updateUser_fromUserTable(User user) {
        boolean result = false;
        User foundUser = getUser_fromUserTable(user.getId());
        
        if(foundUser != null) {
            foundUser.setUsername(user.getUsername());
            foundUser.setPassword(user.getPassword());
            foundUser.setRole(user.getRole());
            result = true;
        }

        return result;
    }

    public boolean deleteUser_fromUserTable(String id) {
        boolean result = false;
        User foundUser = getUser_fromUserTable(id);
        
        if(foundUser != null) {
            table_user.remove(foundUser);
            result = true;
        }
        return result;
    }
    
    // fn to get all orders by user
	public ArrayList<OrderRecord> getOrdersByUser(String id) {
		ArrayList<OrderRecord> orders = new ArrayList<OrderRecord>();
		for (OrderRecord order : getUser_fromUserTable(id).getOrderRecordList()) {
			orders.add(order);
		}
		return orders;
	}
}
