
package Account;

import java.util.List;

public class Login {

 private String username;
 private String password;

 public Login(String username, String password) {
  this.username = username;
  this.password = password;
 }

 public void CheckLogin(List<Account> accountList) {
  for(Account account : accountList) {
    if(account.getUsername().equals(username) && account.getPassword().equals(password)) {
        System.out.println("Login Successful");
        return;
    }
    
  }
  System.out.println("Login Failed");
  return;
 }
 
 
}
