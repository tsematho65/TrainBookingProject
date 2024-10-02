
package Account;

import java.util.List;

public class RegisterAccount {

 private Account account;

 public RegisterAccount(String username, String password, String email, String phone, String type) {
    this.account = new Account(username, password, email, phone, type);
    
    //need to add the account to the List.
 }

 public boolean checkRegister(String username, List<Account> accountList) {
    for(Account account : accountList) {
        if(account.getUsername().equals(username)) {
            System.out.println("Username already exists");
            return false;
        }
    }

    accountList.add(account);
    System.out.println("Account created successfully");
    return true;
 }
}
