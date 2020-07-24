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

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.postgresql.util.PSQLException;

import autodidactics.centralpackage.CustomerService;
import autodidactics.centralpackage.Scannable;
import autodidactics.models.Customer;

public class CustomerDaoImpl implements CustomerDao, Scannable {
	
	final static Logger logger = Logger.getLogger(CustomerDaoImpl.class);
	
	@Override
	public void doDatabaseDepositOrWithdraw(String actor, double balanceAfterAction) {
		logger.setLevel(Level.INFO);
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
			e.printStackTrace();
		}
	
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
			e.printStackTrace();
		}
		return valueList;
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
		e.printStackTrace();
		} catch (SQLException e) {
		e.printStackTrace();
		
			}
		return fundslist;
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
			e.printStackTrace();
		}
		return valueList;
	}

	public void createJointProfile(String jointaccountname, String customer1, String customer2) {
		
		LinkageDao l = new LinkageDao();
		try {
			Connection conn = DriverManager.getConnection(l.getDbUrl(), l.getDbUsername(), l.getDbPassword());
			String sql = "{ ? = call jointAccount_insert(?, ?, ?)}";
			CallableStatement cs = conn.prepareCall(sql);

		cs.registerOutParameter(1, Types.VARCHAR);
		cs.setString(2, jointaccountname);
		cs.setString(3, customer1);
		cs.setString(4, customer2);
		
		cs.execute();
		} catch (PSQLException e) {
			System.out.println("=== INFORMATION UPDATED ===");;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean accessJointAccount(String jointaccountname, String customer, String jointowner) {
		
			LinkageDao l = new LinkageDao();
			
			boolean result = false;
			try {
				Connection conn = DriverManager.getConnection(l.getDbUrl(), l.getDbUsername(), l.getDbPassword());
				String sql = "SELECT * FROM jointAccount where jointname = ? AND custusername1 = ? OR custusername2 = ?";
				
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setString(1, jointaccountname);
				ps.setString(2, customer);
				ps.setString(3, jointowner);
				
				ResultSet rs = ps.executeQuery();			
				
				while(rs.next()) {
					
					if (jointaccountname.equals(rs.getString("jointname")) && 
							customer.equals(rs.getString("custusername1")) || 
							jointowner.equals(rs.getString("custusername2"))) {
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
		
	
	public void jointAccountMenu(String s, String jointAccountName) {
		
		System.out.println("\n");
		System.out.println("Hi, " + s + " within " + jointAccountName + "...\n");
		System.out.println(" Welcome to the Joint Account Dashboard!");
		System.out.println(" =======================================");
		System.out.println(" + + + + + + + + + + + + + + + + + + + +");
		System.out.println("\n");
		System.out.println("What would you like to do today?");
		System.out.println("\n \t 1 ≈ WITHDRAW FUNDS"
				+ "\n \t 2 ≈ DEPOSIT FUNDS"
				+ "\n \t 3 ≈ TRANSFER FUNDS\n \t"
				+ " ================"
				+ "\n \t4 ≈ RETURN TO CUSTOMER MENU");
		
		String jMenuSelection = scan.nextLine();
		
		switch(jMenuSelection) {
		
		case"1":
			
			try {
				
				String result = String.join(",", retrieveCustomerBalance(jointAccountName));
				System.out.println(jointAccountName + " joint account balance is $" + result);
				double currentFunds = Double.parseDouble(result);
				System.out.println("Please select a withdraw amount.");
				String withdrawString = scan.nextLine();
				double withdraw = Double.parseDouble(withdrawString);
				if (withdraw < 0) {
					throw new NumberFormatException();
				}
				double withdrawDifference;
				withdrawDifference = currentFunds - withdraw;
				
				doDatabaseDepositOrWithdraw(jointAccountName, withdrawDifference);
				System.out.println(jointAccountName + ", your joint account balance is now $" + withdrawDifference);
				
			} catch (NumberFormatException e) {
				System.out.println("Invalid Entry. Operation terminated(3)...");
				jointAccountMenu(s, jointAccountName);
				break;
			}
				jointAccountMenu(s, jointAccountName);
				break;
			
		case"2":
			try {
				String result2 = String.join(",", retrieveCustomerBalance(jointAccountName));
				System.out.println(jointAccountName + " joint account balance is $" + result2);
				double currentFunds2 = Double.parseDouble(result2);
				System.out.println("Please select a deposit amount.");
				String depositString = scan.nextLine();
				double deposit = Double.parseDouble(depositString);
				if (deposit < 0) {
					throw new NumberFormatException();
				}
				double depositDifference;
				depositDifference = currentFunds2 + deposit;
				
				doDatabaseDepositOrWithdraw(jointAccountName, depositDifference);
				System.out.println(jointAccountName + ",your joint account balance is now $" + depositDifference);
				
			} catch (NumberFormatException e) {
				System.out.println("Invalid Entry. Operation terminated(3)...");
				jointAccountMenu(s, jointAccountName);
				break;
			}
				jointAccountMenu(s, jointAccountName);
				break;
			
		case"3":
			try {
				String customerTransferingFunds = jointAccountName;
				String transferrerResult = String.join(",", retrieveCustomerBalance(customerTransferingFunds));
				System.out.println("Your current balance is $" + transferrerResult);
				double transfererFunds = Double.parseDouble(transferrerResult);
				
				System.out.println("To whose account which you'd like to transfer funds?");
				String customerReceivingFunds = scan.nextLine();
				String receiverResult = String.join(",", retrieveCustomerBalance(customerReceivingFunds));
				double receiverFunds = Double.parseDouble(receiverResult);
			
				System.out.println(customerReceivingFunds + "'s current balance is $" + receiverResult);
				
				System.out.println("Please select a transfer amount.");
				String transferString = scan.nextLine();
				double transfer = Double.parseDouble(transferString);
				if (transfer < 0) {
					throw new NumberFormatException();
				}
				double transferrerFundsDifference;
				double receiverFundsSum;
				transferrerFundsDifference = transfererFunds - transfer;
				receiverFundsSum = receiverFunds + transfer;
				
				doDatabaseDepositOrWithdraw(customerTransferingFunds, transferrerFundsDifference);
				doDatabaseDepositOrWithdraw(customerReceivingFunds, receiverFundsSum);
				System.out.println(customerTransferingFunds + "'s current balance is $" + transferrerFundsDifference);
				System.out.println(customerReceivingFunds + "'s current balance is $" + receiverFundsSum);


			} catch (NumberFormatException e) {
				System.out.println("Invalid Entry. Operation terminated(3)...");
				jointAccountMenu(s, jointAccountName);
				break;
			}
				jointAccountMenu(s, jointAccountName);
				break;
		
		case"4":
			CustomerService.customerDashboard(s);
			CustomerService.customerActions(s);
			break;
		default:
			jointAccountMenu(s, jointAccountName);
			break;
		}
	}	
}
