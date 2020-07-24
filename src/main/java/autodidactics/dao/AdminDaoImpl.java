package autodidactics.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.postgresql.util.PSQLException;

import autodidactics.centralpackage.AdminService;
import autodidactics.centralpackage.Scannable;
import autodidactics.models.Customer;
import autodidactics.dao.LinkageDao;

public class AdminDaoImpl implements CustomerDao, EmployeeDao, AdminDao, Scannable {
	
	final static Logger logger = Logger.getLogger(AdminDaoImpl.class);

	public void withdrawFunds(Customer taker, double withdraw) {
		double currentFunds = taker.getAccountBalance();
		if (taker.getAccountBalance() - withdraw < 0) {
			System.out.println("That would be an overdraw!");
			System.out.println("Please try again!");
			return;
		}
		taker.setAccountBalance(currentFunds -= withdraw);
		doDatabaseDepositOrWithdraw(taker.getUsername(), currentFunds);
		System.out.println("Withdraw complete! \n The account balance is now $" + currentFunds + "0");
	}
	
	public void depositFunds(Customer sender, double deposit) {

		double currentFunds = sender.getAccountBalance();
		sender.setAccountBalance(currentFunds += deposit);
		doDatabaseDepositOrWithdraw(sender.getUsername(), currentFunds);
		System.out.println("Deposit complete! \n The account balance is now $" + currentFunds + "0");
	}	
	
	public void transferFunds(Customer sender, Customer taker, double transfer) {
		double senderFunds = sender.getAccountBalance();
		double takerFunds = taker.getAccountBalance();
		if (sender.getAccountBalance() - transfer < 0) {
			System.out.println("That would be an overdraw!");
			System.out.println("Please try again!");
			
			return;
		}
		sender.setAccountBalance(senderFunds -= transfer);
		taker.setAccountBalance(takerFunds += transfer);
		doDatabaseDepositOrWithdraw(sender.getUsername(), senderFunds);
		doDatabaseDepositOrWithdraw(taker.getUsername(), takerFunds);
	}

	@Override
	public List<String> retrieveCustomerInfo(String selection) {
		
		
			LinkageDao l = new LinkageDao();
			Connection conn;
			List<String> valueList = new ArrayList<>();
			
			try {
				conn = DriverManager.getConnection(l.getDbUrl(), l.getDbUsername(), l.getDbPassword());
				String sql = "SELECT * FROM customers WHERE username = ?";
				PreparedStatement ps = conn.prepareStatement(sql);
				
				ps.setString(1, selection);
				
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					valueList.add(rs.getString(1));
					valueList.add(rs.getString(2));
					valueList.add(rs.getString(3));
					valueList.add(rs.getString(4));
					// ADMINISTRATOR MAY SEE THE 4TH COLUMN (PASSWORD)
				}
			} catch (PSQLException e) {
				logger.info("PSQLException: no return; no issue");
			} catch (SQLException e) {
				logger.info("SQLException @ retrieveCustomerInfo");
				e.printStackTrace();

		}
		return valueList;
	}
			
	public void cancelAccount(String usernameToClose, Map<String, Customer> customerPool) {
		
		if (customerPool.containsKey(usernameToClose) ) {
			customerPool.remove(usernameToClose);
			deleteCustomer(usernameToClose);
			System.out.println("Account " + usernameToClose + " Removed");
		} else {
			System.out.println("Invalid input; no user by that Identifier found.");
		}
	}

	@Override
	public void doDatabaseDepositOrWithdraw(String actor, double balanceAfterAction) {
		LinkageDao l = new LinkageDao();
		Connection conn;
		try {
			conn = DriverManager.getConnection(l.getDbUrl(), l.getDbUsername(), l.getDbPassword());
			String sql = "update accountBalances set balance = ? where username = ? ";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setDouble(1, balanceAfterAction);
			ps.setString(2, actor);
			ps.execute();
		} catch (SQLException e) {
			logger.info("SQLException @ doDatabaseDepositOrWithdraw");
			e.printStackTrace();

		}
	}
	
	public void deleteCustomer(String username) {
		LinkageDao l = new LinkageDao();
		try {
			Connection conn = DriverManager.getConnection(l.getDbUrl(), l.getDbUsername(), l.getDbPassword());
			String sql = "{ ? = call delete_customer(?)}";
			CallableStatement cs = conn.prepareCall(sql);

		cs.registerOutParameter(1, Types.VARCHAR);
		cs.setString(2, username);
		
		cs.execute();
		System.out.println(cs.getString(1));
		
		} catch (SQLException e) {
			logger.info("SQLException @ deleteCustomer");
			e.printStackTrace();

		}
	}

	public void initializeAccountBalance(String username) {
		
		LinkageDao l = new LinkageDao();
		try {
			Connection conn = DriverManager.getConnection(l.getDbUrl(), l.getDbUsername(), l.getDbPassword());
			String sql2 = "{ ? = call make_balance_table(?, 0.0)}";
			CallableStatement cs2 = conn.prepareCall(sql2);

		cs2.registerOutParameter(1, Types.VARCHAR);
		cs2.setString(2, username);
		
		cs2.execute();
		} catch (PSQLException e) {
			logger.info("PSQLException: no return; no issue");
		} catch (SQLException e) {
			logger.info("SQLException @ initializeAccountBalance");
			e.printStackTrace();

		}
	}

	public List<String> retrieveApprovedApplicantInfo(String selection) {
		LinkageDao l = new LinkageDao();
		Connection conn;
		List<String> valueList = new ArrayList<>();
		
		try {
			conn = DriverManager.getConnection(l.getDbUrl(), l.getDbUsername(), l.getDbPassword());
			String sql = "SELECT * FROM applicants where username = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			
			ps.setString(1, selection);
			
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				valueList.add(rs.getString(1));
				valueList.add(rs.getString(2));
				valueList.add(rs.getString(3));
				valueList.add(rs.getString(4));
			}
			} catch (SQLException e) {
			e.printStackTrace();
		
			}
		return valueList;
	}
	
	public List<String> getApplicantUsernames() {
		LinkageDao l = new LinkageDao();
		Connection conn;
		List<String> valueList = new ArrayList<>();
		
		try {
			conn = DriverManager.getConnection(l.getDbUrl(), l.getDbUsername(), l.getDbPassword());
			String sql = "SELECT username FROM applicants";
			PreparedStatement ps = conn.prepareStatement(sql);
			
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				valueList.add(rs.getString(1));
			}
		} catch (SQLException e) {
			logger.info("SQLException @ getApplicantUsernames");
			e.printStackTrace();


		}
		
		return valueList;
	}
	
	public List<String> getCustomerUsernames() {
		LinkageDao l = new LinkageDao();
		Connection conn;
		List<String> valueList = new ArrayList<>();
		
		try {
			conn = DriverManager.getConnection(l.getDbUrl(), l.getDbUsername(), l.getDbPassword());
			String sql = "SELECT username FROM customers";
			PreparedStatement ps = conn.prepareStatement(sql);
			
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				valueList.add(rs.getString(1));
			}
		} catch (SQLException e) {
			logger.info("SQLException @ getCustomerUsernames");
			e.printStackTrace();

		}
		
		return valueList;
	}

	public List<String> getApplicantTotal() {
		LinkageDao l = new LinkageDao();
		Connection conn;
		List<String> valueList = new ArrayList<>();

		try {
			conn = DriverManager.getConnection(l.getDbUrl(), l.getDbUsername(), l.getDbPassword());
			String sql = "SELECT COUNT (*) FROM applicants";
			PreparedStatement ps = conn.prepareStatement(sql);
			
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				valueList.add(rs.getString(1));
				
				}
		} catch (SQLException e) {
			logger.info("SQLException @ getApplicantTotal");
			e.printStackTrace();

		}
		return valueList;
 	}
	
	public List<String> getCustomerTotal() {
		
		LinkageDao l = new LinkageDao();
		Connection conn;
		List<String> valueList = new ArrayList<>();

		try {
			conn = DriverManager.getConnection(l.getDbUrl(), l.getDbUsername(), l.getDbPassword());
			String sql = "SELECT COUNT (*) FROM customers";
			PreparedStatement ps = conn.prepareStatement(sql);
			
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				valueList.add(rs.getString(1));
				
				}
		} catch (SQLException e) {
			logger.info("SQLException @ getCustomerTotal");
			e.printStackTrace();

		}
		return valueList;
 	}
	
	public Customer returnNewCustomer(String selection) {
		
		LinkageDao l = new LinkageDao();
		Connection conn;
		Customer newCustomer = new Customer();
		
		try {
			conn = DriverManager.getConnection(l.getDbUrl(), l.getDbUsername(), l.getDbPassword());
			String sql = "SELECT * FROM applicants where username = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			
			ps.setString(1,  selection);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				newCustomer = new Customer (rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
			}
//			ps.execute();
		} catch (SQLException e) {
			logger.info("SQLException @ returnNewCustomer");
			e.printStackTrace();

		}
		
		return newCustomer;
		
	}

	public void deleteApplicantRecords(String selection) {
		LinkageDao l = new LinkageDao();
		Connection conn;
		
		try {
			conn = DriverManager.getConnection(l.getDbUrl(), l.getDbUsername(), l.getDbPassword());
			String sql = "DELETE FROM applicants WHERE username = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			
			ps.setString(1, selection);
			ps.executeQuery();
			
			} catch (PSQLException e) {
//				logger.info("PSQLException: no return; no issue");
				e.printStackTrace();
			} catch (SQLException e) {
				logger.info("SQLException @ deleteApplicantRecords");

		
			}
		}

	public void deleteCustomerRecords(String selection) {
		LinkageDao l = new LinkageDao();
		Connection conn;
		
		try {
			conn = DriverManager.getConnection(l.getDbUrl(), l.getDbUsername(), l.getDbPassword());
			String sql = "DELETE FROM customers WHERE username = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			
			ps.setString(1, selection);
			ps.executeQuery();
			
		} catch (PSQLException e) {
			logger.info("PSQLException: no return; no issue");
		} catch (SQLException e) {
			logger.info("SQLException @ deleteCustomerRecords");
			e.printStackTrace();

		
			}
		}

	public void deleteFinancialRecords(String selection) {
		LinkageDao l = new LinkageDao();
		Connection conn;
		
		try {
			conn = DriverManager.getConnection(l.getDbUrl(), l.getDbUsername(), l.getDbPassword());
			String sql = "DELETE FROM accountBalances WHERE username = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			
			ps.setString(1, selection);
			ps.executeQuery();
			
		} catch (PSQLException e) {
			logger.info("PSQLException: no return; no issue");
		} catch (SQLException e) {
			logger.info("SQLException @ deleteFinancialRecords");
			e.printStackTrace();

		
			}
		}
	
	public List<String> retrieveCustomerBalance(String selection) {
		LinkageDao l = new LinkageDao();
		Connection conn;
		List<String> fundslist = new ArrayList<>();
		
		try {
			conn = DriverManager.getConnection(l.getDbUrl(), l.getDbUsername(), l.getDbPassword());
			String sql = "SELECT * FROM accountBalances WHERE username = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			
			ps.setString(1, selection);
//			ps.executeQuery();
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
//				fundslist.add(rs.getString(1));
				fundslist.add(rs.getString(2));
			} 
			
		} catch (PSQLException e) {
			logger.info("PSQLException: no return; no issue");
		} catch (SQLException e) {
			logger.info("SQLException @ retrieveCustomerBalance");
			e.printStackTrace();

		
			}
		return fundslist;
		}
		
	public void approveCustomerBeforeApplicationDeletion(String selection) {
		
		LinkageDao l = new LinkageDao();
		Connection conn;
		Customer newCustomer = new Customer();
		
		try {
			conn = DriverManager.getConnection(l.getDbUrl(), l.getDbUsername(), l.getDbPassword());
			String sql = "INSERT INTO customers(SELECT username, name, email, password FROM applicants WHERE username = ?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			
			ps.setString(1,  selection);
			ps.execute();
//			while (rs.next()) {
//				newCustomer = new Customer (rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
//			}
		} catch (PSQLException e) {
//			logger.info("PSQLException: no return; no issue");
			e.printStackTrace();
		} catch (SQLException e) {
			logger.info("SQLException @ approveCustomerBeforeApplicationDeletion");
			e.printStackTrace();

		}
	}
	
	public List<String> getJointAccountApplications() {
		LinkageDao l = new LinkageDao();
		Connection conn;
		List<String> valueList = new ArrayList<>();
		
		try {
			conn = DriverManager.getConnection(l.getDbUrl(), l.getDbUsername(), l.getDbPassword());
			String sql = "SELECT jointname FROM jointAccount";
			PreparedStatement ps = conn.prepareStatement(sql);
			
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				valueList.add(rs.getString(1));
			}
		} catch (SQLException e) {
			logger.info("SQLException @ getJointAccountApplications");
			e.printStackTrace();

		}
		
		return valueList;
	}
	
	public List<String> retrieveJointInfo(String selection) {
		
		LinkageDao l = new LinkageDao();
		Connection conn;
		List<String> valueList = new ArrayList<>();
		
		try {
			conn = DriverManager.getConnection(l.getDbUrl(), l.getDbUsername(), l.getDbPassword());
			String sql = "SELECT * FROM jointAccount WHERE jointname = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			
			ps.setString(1, selection);
			
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				valueList.add(rs.getString(1));
				valueList.add(rs.getString(2));
				valueList.add(rs.getString(3));
				// ADMINISTRATOR MAY SEE THE 4TH COLUMN (PASSWORD)
			}
		} catch (PSQLException e) {
			logger.info("PSQLException: no return; no issue");
		} catch (SQLException e) {
			logger.info("SQLException @ retrieveJointInfo");
			e.printStackTrace();
		}
	return valueList;
}
	
	public void deleteJointAccountApplication(String selection) {
		LinkageDao l = new LinkageDao();
		Connection conn;
		
		try {
			conn = DriverManager.getConnection(l.getDbUrl(), l.getDbUsername(), l.getDbPassword());
			String sql = "DELETE FROM jointAccount WHERE jointname = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			
			ps.setString(1, selection);
			ps.executeQuery();
			
		} catch (PSQLException e) {
		System.out.println(" === INFORMATION UPDATED === ");
		} catch (SQLException e) {
			logger.info("SQLException @ deleteJointAccountApplication");
			e.printStackTrace();

			}
		}
	
	public static void insertEmployee(Customer applicant) {
			
			LinkageDao l = new LinkageDao();
			try {
				Connection conn = DriverManager.getConnection(l.getDbUrl(), l.getDbUsername(), l.getDbPassword());
				String sql = "{ ? = call employee_insert(?,?,?,?)}";;
				CallableStatement cs = conn.prepareCall(sql);
		
			cs.registerOutParameter(1, Types.VARCHAR);
			cs.setString(2, applicant.getUsername());
			cs.setString(3, applicant.getName());
			cs.setString(4, applicant.getEmail());
			cs.setString(5, applicant.getPassword());
			
			cs.execute();
			System.out.println(cs.getString(1));
			
			} catch (SQLException e) {
				logger.info("SQLException @ insertEmployee");
				e.printStackTrace();
				
			}
	
	}
	
	public static void insertAdmin(Customer applicant) {
		
		LinkageDao l = new LinkageDao();
		try {
			Connection conn = DriverManager.getConnection(l.getDbUrl(), l.getDbUsername(), l.getDbPassword());
			String sql = "{ ? = call admin_insert(?,?,?,?)}";;
			CallableStatement cs = conn.prepareCall(sql);
	
		cs.registerOutParameter(1, Types.VARCHAR);
		cs.setString(2, applicant.getUsername());
		cs.setString(3, applicant.getName());
		cs.setString(4, applicant.getEmail());
		cs.setString(5, applicant.getPassword());
		
		cs.execute();
		System.out.println(cs.getString(1));
		
		} catch (SQLException e) {
			logger.info("SQLException @ insertAdmin");
			e.printStackTrace();

			
		}

	}
	
	public static void applyForEmployeeAccount() {
		
		Customer applicant = new Customer();
		
		System.out.println("Please choose a username: ");
		String acquireUsername = scan.nextLine();
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
		
		insertEmployee(applicant);
		System.out.println(acquireUsername + " is registered!");
		}
		
	public static void applyForAdminAccount() {
		
		Customer applicant = new Customer();
		
		System.out.println("Please choose a username: ");
		String acquireUsername = scan.nextLine();
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
		
		insertAdmin(applicant);
		System.out.println(acquireUsername + " is registered!");
	}
}



