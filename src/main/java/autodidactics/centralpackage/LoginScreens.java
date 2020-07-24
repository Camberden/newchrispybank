package autodidactics.centralpackage;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import autodidactics.centralpackage.UsernameTooShortException;
import autodidactics.dao.LinkageDao;
import autodidactics.dao.CustomerDaoImpl;
import autodidactics.models.Customer;

public abstract class LoginScreens implements Scannable {
	
final static Logger logger = Logger.getLogger(CustomerService.class);

	
	////////////////////////////////////////////////////
	//================================================//
	//||			 	  FIELDS					||//
	//================================================//
	////////////////////////////////////////////////////
		
	//================================================//

	static CustomerDaoImpl cDaoImpl = new CustomerDaoImpl();

	//================================================//

	static Map <String, Customer> customerMap = new HashMap<String, Customer>();

	//================================================//


	////////////////////////////////////////////////////
	//================================================//
	//||			 	  METHODS					||//
	//================================================//
	////////////////////////////////////////////////////

	//================================================//

	public static void customerLoginScreen() {
	
		System.out.println("Hi!\n Please enter your username:\n"+
							" Press 9 to return to the Main Menu.");
		String customerUsername = scan.nextLine();
		if (customerUsername.equals("9")) {
			landingPage();
			return;
		}
		System.out.println("And now input your password.");
		String customerPassword = scan.nextLine();
		
		if  
	    	((verifyUsernameAndPassword(customerUsername, customerPassword))) {
	
	    		System.out.println("ACCESSED CUSTOMER DASHBOARD");
				customerDashboard(customerUsername);
				CustomerService.customerActions(customerUsername);
				
		} else {
	    	System.out.println("Invalid input. Please try again!");
	    	customerLoginScreen();
		}
	}
	
	
	public static void staffLoginScreen() {
	
		
		System.out.println("Welcome. Please input your username.\n"
				+ " Press 9 to return to the Main Menu.");
		String staffUsername = scan.nextLine();
		if (staffUsername.equals("9")) {
			landingPage();
			return;
		}
		System.out.println("And your password, please.");
		String staffPassword = scan.nextLine();
//		staffMap.put(staffUsername, staffPassword);
		
		if (verifyAdminUsernameAndPassword(staffUsername, staffPassword)) {
			
	    	System.out.println("ACCESSED ADMINISTRATOR DASHBOARD");
	    	adminDashboard(staffUsername);
	    	AdminService.adminActions(staffUsername);
	    	
		}else if (verifyEmployeeUsernameAndPassword(staffUsername, staffPassword)) {
			
	    	System.out.println("ACCESSED EMPLOYEE DASHBOARD");
	    	employeeDashboard(staffUsername);
	    	EmployeeService.employeeActions(staffUsername);
	    } else {
	    	System.out.println("Invalid input. Please try again!");
	    	staffLoginScreen();
	    }
	}
	
	
	//================================================//
	
	public static void applyForAccount() {
		Customer applicant = new Customer();
	try {
		System.out.println("Please choose a username: ");
		String acquireUsername = scan.nextLine();
		if (acquireUsername.length() < 3) {
			throw new UsernameTooShortException("Username too short!");
		}
		applicant.setUsername(acquireUsername);
		
		System.out.println("Please input your name: ");
		String acquireName = scan.nextLine();
		applicant.setName(acquireName);

		System.out.println("Please input your email: ");
		String acquireEmail = scan.nextLine();
		applicant.setEmail(acquireEmail);

		System.out.println("Please create a password: ");
		String acquirePassword = scan.nextLine();
		applicant.setPassword(acquirePassword);
		
		insertUser(applicant);
		System.out.println("Hi " + acquireUsername + ".\n You're registered!");
		System.out.println("Would you like to apply for an account?   Y/N");
		String confirmation = scan.nextLine();
		switch(confirmation) {
		case "Y":
			insertApplicant(applicant);
			System.out.println("Application Sent!");
		default:
			break;
		}
		
	}catch (UsernameTooShortException e) {
		System.out.println("Username too short!");
		}
			
	}
	
	
	//================================================//
	
	
	
	public static void landingPage() {
	
	
		
		System.out.println(" + + + + + + + + + + + + + + + + + +");
		System.out.println("+ ================================= +");
		System.out.println("+ || Welcome to New Chrispy Bank!|| +");
		System.out.println("+ ================================= +");
		System.out.println(" + + + + + + + + + + + + + + + + + +");
		
		
		System.out.println("\n \n Please select from the following:");
		System.out.println(" ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯");
		System.out.println("\n \t 1 ≈ RETURNING CUSTOMER "
						+ "\n \t 2 ≈ NEW USER/CUSTOMER \n \t"
						+ " ================"
						+ "\n \t 3 ≈ EMPLOYEE "
						+ "\n \t 4 ≈ ADMINISTRATOR \n \t"
						+ " ================ \n \t 5 ≈ QUIT");
		}
	
	
	public static void terminateSession() {
		
		System.out.println(" === Application Closed === \n \n \t ...Goodbye!");                                   
		System.exit(0);
		
		}
	
	
	//================================================//
	
	
	public static void customerDashboard(String s)  {
	
		
		System.out.println("\n");
		System.out.println("Hi, " + s + "...\n");
//		System.out.println("CURRENT BALANCE: " + "$" + customerMap.get(s).getAccountBalance()+ "0" 
//		+  "\n" + "YOUR INFO: " + customerMap.get(s));
		System.out.println(" Welcome to the Customer Dashboard!");
		System.out.println(" =================================");
		System.out.println(" + + + + + + + + + + + + + + + + +");
		System.out.println("\n");
		System.out.println("What would you like to do today?");
		System.out.println("\n \t 1 ≈ WITHDRAW FUNDS"
				+ "\n \t 2 ≈ DEPOSIT FUNDS\n \t"
				+ " 3 ≈ TRANSFER FUNDS\n \t"
				+ " ================\n \t"
				+ " 4 ≈ APPLY FOR JOINT ACCOUNT\n \t"
				+ " 5 ≈ ACCESS JOINT ACCOUNT\n \t"
				+ " ================"
				+ "\n \t 6 ≈ SIGN OUT");
		}
	
	
	public static void employeeDashboard(String s) {
		
		System.out.println("\n");
		System.out.println("Hi there, " + s + "...\n");
		System.out.println(" Welcome to the Employee Dashboard!");
		System.out.println(" =================================");
		System.out.println(" + + + + + + + + + + + + + + + + +");
		System.out.println("\n");
		System.out.println("What would you like to do today?");
		System.out.println("\n \t 1 ≈ RETRIEVE CUSTOMER INFO/BALANCES \n \t"
				+ " ================"
				+ "\n \t 2 ≈ APPROVE/REJECT ACCOUNT APPLICATIONS \n \t"
				+ " ================"
				+ "\n \t 3 ≈ SIGN OUT");
	
	}
	
	
	public static void adminDashboard(String s) {
	
	
		System.out.println("\n");
		System.out.println("Hi there, " + s + "...\n");
		System.out.println(" Welcome to the Administrator Dashboard!");
		System.out.println(" =======================================");
		System.out.println(" + + + + + + + + + + + + + + + + + + + +");
		System.out.println("\n");
		System.out.println("What would you like to do today?");
		System.out.println("\n \t 1 ≈ WITHDRAW CUSTOMER FUNDS"
				+ "\n \t 2 ≈ DEPOSIT INTO CUSTOMER ACCOUNTS"
				+ "\n \t 3 ≈ TRANSFER AMONG CUSTOMER ACCOUNTS \n \t"
				+ " ================"
				+ "\n \t 4 ≈ RETRIEVE CUSTOMER INFO/BALANCES \n \t"
				+ " ================"
				+ "\n \t 5 ≈ APPROVE/REJECT ACCOUNT APPLICATIONS"
				+ "\n \t 6 ≈ APPROVE/REJECT JOINT ACCOUNT APPLICATIONS"
				+ "\n \t 7 ≈ ADD EMPLOYEE OR ADMINISTRATOR ACCOUNT"
				+ "\n \t 8 ≈ CLOSE ACCOUNTS \n \t"
				+ " ================"
				+ "\n \t 9 ≈ SIGN OUT");
		
	}
	
	
	
	//================================================//
	
	
	protected static double inputToDouble(String s) {
		
		double input = Integer.parseInt(s);
		return input;
		}
	
	
	public static boolean verifyUsernameAndPassword(String username, String password) {
		
		LinkageDao l = new LinkageDao();
		
		boolean result = false;
		try {
			Connection conn = DriverManager.getConnection(l.getDbUrl(), l.getDbUsername(), l.getDbPassword());
			String sql = "SELECT username, password FROM customers where username = ? AND password = ?";
			
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			ps.setString(2, password);
			
			ResultSet rs = ps.executeQuery();			
			
			while(rs.next()) {
				
				if (username.equals(rs.getString("username")) && password.equals(rs.getString("password"))) {
						result = true;
	                    System.out.println("Logged in!");
	                    break;
	                } else {
	                    System.out.println("Password did not match username!");
	                    System.out.println("Username did not match the database");
	//                    customerLoginScreen();
	                    break;
	                }  
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}


	public static boolean verifyEmployeeUsernameAndPassword(String username, String password) {
		
		LinkageDao l = new LinkageDao();
		
		boolean result = false;
		try {
			Connection conn = DriverManager.getConnection(l.getDbUrl(), l.getDbUsername(), l.getDbPassword());
			String sql = "SELECT username, password FROM employees where username = ? AND password = ?";
			
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			ps.setString(2, password);
			
			ResultSet rs = ps.executeQuery();			
			
			while(rs.next()) {
				
				if (username.equals(rs.getString("username")) && password.equals(rs.getString("password"))) {
						result = true;
	                    System.out.println("Logged in!");
	                    break;
	                } else {
	                    System.out.println("Password did not match username!");
	                    System.out.println("Username did not match the database");
	//                    customerLoginScreen();
	                    break;
	                }  
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}


	public static boolean verifyAdminUsernameAndPassword(String username, String password) {
		
		LinkageDao l = new LinkageDao();
		
		boolean result = false;
		try {
			Connection conn = DriverManager.getConnection(l.getDbUrl(), l.getDbUsername(), l.getDbPassword());
			String sql = "SELECT username, password FROM administrators where username = ? AND password = ?";
			
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			ps.setString(2, password);
			
			ResultSet rs = ps.executeQuery();			
			
			while(rs.next()) {
				
				if (username.equals(rs.getString("username")) && password.equals(rs.getString("password"))) {
						result = true;
	                    System.out.println("Logged in!");
	                    break;
	                } else {
	                    System.out.println("Password did not match username!");
	                    System.out.println("Username did not match the database");
	//                    customerLoginScreen();
	                    break;
	                }  
			}
		} catch (SQLException e) {
			logger.info("SQLException @ verifyAdminUsernameAndPassword");
			e.printStackTrace();
		}
		return result;
	}


	public static void insertApplicant(Customer applicant) {
		
		LinkageDao l = new LinkageDao();
		try {
			Connection conn = DriverManager.getConnection(l.getDbUrl(), l.getDbUsername(), l.getDbPassword());
			String sql = "{ ? = call applicant_insert(?,?,?,?)}";;
			CallableStatement cs = conn.prepareCall(sql);
	
		cs.registerOutParameter(1, Types.VARCHAR);
		cs.setString(2, applicant.getUsername());
		cs.setString(3, applicant.getName());
		cs.setString(4, applicant.getEmail());
		cs.setString(5, applicant.getPassword());
		
		cs.execute();
		System.out.println(cs.getString(1));
		
		} catch (SQLException e) {
			e.printStackTrace();
			
			
		}
	}
	
	public static void insertUser(Customer applicant) {
		
		LinkageDao l = new LinkageDao();
		try {
			Connection conn = DriverManager.getConnection(l.getDbUrl(), l.getDbUsername(), l.getDbPassword());
			String sql = "{ ? = call user_insert(?,?,?,?)}";;
			CallableStatement cs = conn.prepareCall(sql);
	
		cs.registerOutParameter(1, Types.VARCHAR);
		cs.setString(2, applicant.getUsername());
		cs.setString(3, applicant.getName());
		cs.setString(4, applicant.getEmail());
		cs.setString(5, applicant.getPassword());
		
		cs.execute();
		System.out.println(cs.getString(1));
		
		} catch (SQLException e) {
			e.printStackTrace();
			
			
		}
	}
}

