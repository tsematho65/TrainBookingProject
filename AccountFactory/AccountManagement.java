package AccountFactory;

import java.util.List;

public class AccountManagement {

	public List<Account> accountList;
	private AccountFactory accountFactory = new AccountFactory();
	
	public AccountManagement(List<Account> accountList) {
		this.accountList = accountList;
		
	}
	
	public void addAccount(String username, String password, String email, String phone, String type) {
		if(checkRegister(username, accountList)){
			if(type.equals("user")) {
				accountList.add(accountFactory.createUser(username, password, email, phone));
			}
			else if(type.equals("admin")) {
				accountList.add(accountFactory.createAdmin(username, password, email, phone));
			}
			else {
				System.out.println("Invalid account type");
			}
		}else{
			System.out.println("Account already exists");	
		}
		//accountList.add(new Account(username, password, email, phone, type));
	}

	public boolean checkRegister(String username, List<Account> accountList) {
		for(Account account : accountList) {
			if(account.getUsername().equals(username)) {
				System.out.println("Username already exists");
				return false;
			}
		}
		return true;
	 }
	
	public void removeAccount(String username) {
		for (Account account : accountList) {
			if (account.getUsername().equals(username)) {
				accountList.remove(account);
				System.out.println("Account removed successfully");
				return;
			}
		}
		System.out.println("Account not found");
	}
	
	public void updatePassword(String username, String password) {
		for (Account account : accountList) {
			if (account.getUsername().equals(username)) {
				account.setPassword(password);
				System.out.println("Password updated successfully");
				return;
			}
		}
		System.out.println("Account not found");
	}
	
	public void updateEmail(String username, String email) {
		for (Account account : accountList) {
			if (account.getUsername().equals(username)) {
				account.setEmail(email);
				System.out.println("Email updated successfully");
				return;
			}
		}
		System.out.println("Account not found");
	}
	
	public void updatePhone(String username, String phone) {
		for (Account account : accountList) {
			if (account.getUsername().equals(username)) {
				account.setPhone(phone);
				System.out.println("Phone updated successfully");
				return;
			}
		}
		System.out.println("Account not found");
	}
	
	public void updateType(String username, String type) {
		for (Account account : accountList) {
			if (account.getUsername().equals(username)) {
				account.setType(type);
				System.out.println("Type updated successfully");
				return;
			}
		}
		System.out.println("Account not found");
	}
	
	public void listAccounts() {
		for (Account account : accountList) {
			System.out.println(account.getUsername() + " " + account.getEmail() + " " + account.getPhone() + " "
					+ account.getType());
		}
	}
	
	
	
	
	
}
