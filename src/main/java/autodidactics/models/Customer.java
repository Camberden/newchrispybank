package autodidactics.models;

public class Customer {
	
	
	public Customer (String username, String name, String email, String password) {
		
	}
	
	public Customer() {
		
	}
	
	private String username;
	private String name;
	private String email;
	private String password;
	private double accountBalance;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	public double getAccountBalance() {
		return accountBalance;
	}
	public void setAccountBalance(double accountBalance) {
		this.accountBalance = accountBalance;
	}
	@Override
	public String toString() {
		return " Name = " + name + " | Email = " + email + " | Username = " + username + " | Password = " + password;
	}																					// toPasswordHash removed!
	
	public String toStringForAdmin() {
		return " Name = " + name + " | Email = " + email + " | Username = " + username + " | Password = " + password;
	}
	
//	public String toPasswordHash(String credentials) {
//	String passwordHash = "";
//	for (int i = 0; i < credentials.length(); i++) {
//		passwordHash += "#";
//		}
//	return passwordHash;
//	}
}
