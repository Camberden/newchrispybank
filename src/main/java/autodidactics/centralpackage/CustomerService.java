package autodidactics.centralpackage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import autodidactics.dao.AdminDaoImpl;
import autodidactics.dao.CustomerDaoImpl;
import autodidactics.dao.LinkageDao;
import autodidactics.models.Customer;

public class CustomerService extends LoginScreens {
	
	final static Logger logger = Logger.getLogger(CustomerService.class);

	public static void customerActions(String s) {
		logger.setLevel(Level.INFO);
		CustomerDaoImpl cDao = new CustomerDaoImpl();
		
		String dashboardSelection = scan.nextLine();
		switch(dashboardSelection) {
		
		case "1":
		
			try {
				String result = String.join(",", cDao.retrieveCustomerBalance(s));
				System.out.println(s + ", current balance is $" + result);
				double currentFunds = Double.parseDouble(result);
				System.out.println("Please select a withdraw amount.");
				String withdrawString = scan.nextLine();
				double withdraw = Double.parseDouble(withdrawString);
				if (withdraw < 0) {
					throw new NumberFormatException();
				}
				double withdrawDifference;
				withdrawDifference = currentFunds - withdraw;
				if (withdrawDifference < 0) {
					throw new NumberFormatException();
				}
				
				cDao.doDatabaseDepositOrWithdraw(s, withdrawDifference);
				System.out.println(s + ", your current balance is now $" + withdrawDifference);
				logger.info(s + " withdrew $" + withdraw);
				
			} catch (NumberFormatException e) {
				System.out.println("Invalid Entry. Operation terminated(3)...");
				customerDashboard(s);
				customerActions(s);
				break;
			}
				customerDashboard(s);
				customerActions(s);
				break;
				
		case "2":
			
			try {
				String result2 = String.join(",", cDao.retrieveCustomerBalance(s));
				System.out.println(s + ", your current balance is $" + result2);
				double currentFunds2 = Double.parseDouble(result2);
				System.out.println("Please select a deposit amount.");
				String depositString = scan.nextLine();
				double deposit = Double.parseDouble(depositString);
				if (deposit < 0) {
					throw new NumberFormatException();
				}
				double depositDifference;
				depositDifference = currentFunds2 + deposit;
				
				cDao.doDatabaseDepositOrWithdraw(s, depositDifference);
				System.out.println(s + ",your current balance is now $" + depositDifference);
				logger.info(s + " deposited $" + deposit);

		} catch (NumberFormatException e) {
			System.out.println("Invalid Entry. Operation terminated(3)...");
			customerDashboard(s);
			customerActions(s);
			break;
		}
			customerDashboard(s);
			customerActions(s);
			break;
		
		case "3":
		// TRY FOR SQL EXCEPTION
			try {
				String customerTransferingFunds = s;
				String transferrerResult = String.join(",", cDao.retrieveCustomerBalance(customerTransferingFunds));
				System.out.println("Your current balance is $" + transferrerResult);
				double transfererFunds = Double.parseDouble(transferrerResult);
				
				System.out.println("To whose account which you'd like to transfer funds?");
				String customerReceivingFunds = scan.nextLine();
				String receiverResult = String.join(",", cDao.retrieveCustomerBalance(customerReceivingFunds));
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
				
				cDao.doDatabaseDepositOrWithdraw(customerTransferingFunds, transferrerFundsDifference);
				cDao.doDatabaseDepositOrWithdraw(customerReceivingFunds, receiverFundsSum);
				System.out.println(customerTransferingFunds + "'s current balance is $" + transferrerFundsDifference);
				System.out.println(customerReceivingFunds + "'s current balance is $" + receiverFundsSum);
				logger.info(customerTransferingFunds + " transferred $" + transfer + " to " + customerReceivingFunds);


			} catch (NumberFormatException e) {
				System.out.println("Invalid Entry. Operation terminated(3)...");
				customerDashboard(s);
				customerActions(s);
				break;
			}
				customerDashboard(s);
				customerActions(s);
				break;
				
		case "4":
			// APPLY FOR JOINT ACCOUNT
			
			System.out.println("With whom would you like to open a joint account?");
			String selection = scan.nextLine();
			
			System.out.println(selection);
			if (cDao.getCustomerUsernames().contains(selection)) {
				
				System.out.println("You plan to open an account with " + selection);
				System.out.println("Please provide a unique joint account name.");
				String jointAccountName = scan.nextLine();
				
				cDao.createJointProfile(jointAccountName, s, selection);
				System.out.println("Joint Account Application Sent!");
				
			} else {
				System.out.println("No such username found. Operation terminated...");
			}
				customerDashboard(s);
				customerActions(s);
				break;
				
		case "5":
			
				
				System.out.println("Please input your jointaccount name:");
				String jointAccountInput = scan.nextLine();
				System.out.println("Please enter the designated joint owner of this account:");
				String jointOwnerInput = scan.nextLine();
				
				
				
				if (cDao.accessJointAccount(jointAccountInput, s, jointOwnerInput)) {
					System.out.println("Entering Joint Account Dashboard");
					cDao.retrieveCustomerBalance(jointAccountInput);
					cDao.jointAccountMenu(s, jointAccountInput);

					
				} else {
					System.out.println("Invalid Input. Operation terminated...");
					customerDashboard(s);
					customerActions(s);
					break;
				}
				
				
		case "6":
			landingPage();
			break;
				
		default:
			customerActions(s);
			break;
		}
	}
}