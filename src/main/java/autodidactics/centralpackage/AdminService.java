package autodidactics.centralpackage;

import autodidactics.models.Customer;

import java.util.List;

import org.apache.log4j.Logger;
import org.postgresql.util.PSQLException;

import autodidactics.centralpackage.LoginScreens;
import autodidactics.dao.AdminDaoImpl;

public class AdminService extends LoginScreens implements Scannable {
	
	final static Logger logger = Logger.getLogger(AdminService.class);

	
	protected static void adminActions(String s) {
		
		AdminDaoImpl aDao = new AdminDaoImpl();
		List<String> customertotal = aDao.getCustomerTotal();
		String customertotalstring = customertotal.get(0);
		int customertotalint = Integer.parseInt(customertotalstring);
		
		String dashboardSelection = scan.nextLine();
		switch (dashboardSelection) {
		
		case "1":
		// WITHDRAW MONEY ( X )
		// CUSTOM EXCEPTION IF NUMBER INPUT IS LESS THAN 0
			
			try {
				if (customertotalint == 0) {
					System.out.println("There are no users at this time! Operation terminated(6)...");
					adminDashboard(s);
					adminActions(s);
					break;
				}
				System.out.println("Please input an account from which you'd like to withdraw:");
				String customerSelection = scan.nextLine();
				
				String result = String.join(",", aDao.retrieveCustomerBalance(customerSelection));
				System.out.println(customerSelection + "'s current balance is $" + result);
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
				
				aDao.doDatabaseDepositOrWithdraw(customerSelection, withdrawDifference);
				
				System.out.println(customerSelection + "'s current balance is now $" + withdrawDifference);
				logger.info(s + " withdrew $" + withdraw + " from " + customerSelection);

				
			} catch (NumberFormatException e) {
				logger.info("NumberFormatException at Applicant Approval; Invalid Entry");				
				adminDashboard(s);
				adminActions(s);
			} catch (NullPointerException e) {
				logger.info("NullPointerException at Applicant Approval; No Applicant with Such Identifier");
				adminDashboard(s);
				adminActions(s);
			}
			adminDashboard(s);
			adminActions(s);
			break;
			
			
		case "2":
		// DEPOSIT MONEY ( X )
			
			try {
				if (customertotalint == 0) {
					System.out.println("There are no users at this time! Operation terminated(6)...");
					adminDashboard(s);
					adminActions(s);
					break;
				}
				System.out.println("Please input an account into which you'd like to deposit:");
				String customerSelection = scan.nextLine();
				
				String result = String.join(",", aDao.retrieveCustomerBalance(customerSelection));
				System.out.println(customerSelection + "'s current balance is $" + result);
				double currentFunds = Double.parseDouble(result);
				System.out.println("Please select a deposit amount.");
				String depositString = scan.nextLine();
				double deposit = Double.parseDouble(depositString);
				if (deposit < 0) {
					throw new NumberFormatException();
				}
				double depositSum;
				depositSum = currentFunds + deposit;
				
				aDao.doDatabaseDepositOrWithdraw(customerSelection, depositSum);
				
				System.out.println(customerSelection + "'s current balance is now $" + depositSum);
				logger.info(s + " deposited $" + deposit + " for " + customerSelection);

				
			} catch (NumberFormatException e) {
				logger.info("NumberFormatException at Applicant Approval; Invalid Entry");				
				adminDashboard(s);
				adminActions(s);
			} catch (NullPointerException e) {
				logger.info("NullPointerException at Applicant Approval; No Applicant with Such Identifier");
				adminDashboard(s);
				adminActions(s);
			}
			adminDashboard(s);
			adminActions(s);
			break;
			
		case "3":
		// TRANSFER MONEY ( X )
			
			try {
				if (customertotalint <= 1) {
					System.out.println("There need to be at least two customers to initiate a transfer! Operation Terminated(8)...");
					adminDashboard(s);
					adminActions(s);
					break;
				}
			
				System.out.println("From whose account which you'd like to transfer funds?");
				String customerTransferingFunds = scan.nextLine();
				String transferrerResult = String.join(",", aDao.retrieveCustomerBalance(customerTransferingFunds));
				System.out.println(customerTransferingFunds + "'s current balance is $" + transferrerResult);
				double transfererFunds = Double.parseDouble(transferrerResult);
				
				System.out.println("To whose account would you like to transfer funds?");
				String customerReceivingFunds = scan.nextLine();
				String receiverResult = String.join(",", aDao.retrieveCustomerBalance(customerReceivingFunds));
				System.out.println(customerReceivingFunds + "'s current balance is $" + receiverResult);
				double receiverFunds = Double.parseDouble(receiverResult);
				
				
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
				
				aDao.doDatabaseDepositOrWithdraw(customerTransferingFunds, transferrerFundsDifference);
				aDao.doDatabaseDepositOrWithdraw(customerReceivingFunds, receiverFundsSum);
				
				System.out.println(customerTransferingFunds + "'s current balance is now $" + transferrerFundsDifference);
				System.out.println(customerReceivingFunds + "'s current balance is now $" + receiverFundsSum);
				logger.info(s + " initialized transfer such that: " + customerTransferingFunds + " transfers $" + transfer + " to " + customerReceivingFunds);

				
			} catch (NumberFormatException e) {
				logger.info("NumberFormatException at Applicant Approval; Invalid Entry");				
				adminDashboard(s);
				adminActions(s);
			} catch (NullPointerException e) {
				logger.info("NullPointerException at Applicant Approval; No Applicant with Such Identifier");
				adminDashboard(s);
				adminActions(s);
			}
			adminDashboard(s);
			adminActions(s);
			break;
		
		case "4":
		// PRESENT CUSTOMER DETAILS ( X )
		try {
			if (customertotalint == 0) {
				System.out.println("There are no customers at this time! Operation terminated(1)...");
				adminDashboard(s);
				adminActions(s);
				break;
			}
			System.out.println("Customer Data:\n");
			System.out.println("Select a customer to present his/her details:");
			System.out.println(aDao.getCustomerUsernames());
			String detailsSelection = scan.nextLine();
			System.out.println("Username | Name | Email | Password");
			System.out.println(aDao.retrieveCustomerInfo(detailsSelection));
			String result = String.join(",", aDao.retrieveCustomerBalance(detailsSelection));
			double currentFunds = Double.parseDouble(result);
			System.out.println(detailsSelection + "'s current balance is $" + currentFunds);
		} catch (NumberFormatException e) {
			logger.info("NumberFormatException at Applicant Approval; Invalid Entry");				
			adminDashboard(s);
			adminActions(s);
		} catch (NullPointerException e) {
			logger.info("NullPointerException at Applicant Approval; No Applicant with Such Identifier");
			adminDashboard(s);
			adminActions(s);
			break;	
		}
		adminDashboard(s);
		adminActions(s);
		break;	
		
		case "5":
		// APPLICANT APPROVAL ( )
			
			List<String> yieldlist = aDao.getApplicantTotal();
			String yieldstring = yieldlist.get(0);
			int yieldint = Integer.parseInt(yieldstring);
			System.out.println("Currently "+ yieldstring + " Account(s) Requested\n");
			
			try {
				if (yieldint == 0) {
					System.out.println("There are no applicants at this time! Operation terminated(1)...");
					adminDashboard(s);
					adminActions(s);
					break;
				}
				
				System.out.println("Please choose the username corresponding to the "
						+ "Application on which you'd like to operate:");
				System.out.println(aDao.getApplicantUsernames());
				String applicantToProcess = scan.nextLine();
				
				System.out.println("Approve applicant " + applicantToProcess + "?");
				System.out.println("Press 1 to Approve.");
				System.out.println("Press 2 to Reject.");
				String decision = scan.nextLine();
				
				switch (decision) {
				case "1":
					aDao.approveCustomerBeforeApplicationDeletion(applicantToProcess);
					aDao.initializeAccountBalance(applicantToProcess);
					aDao.deleteApplicantRecords(applicantToProcess);
					System.out.println("Account Approved!");
					break;
				case "2":
					aDao.deleteApplicantRecords(applicantToProcess);
					System.out.println("Account Request Denied!");
					break;
				
				default:
					System.out.println(("Invalid Input. Operation Terminated..."));
					adminDashboard(s);
					adminActions(s);
					break;
					
				}
					
				adminDashboard(s);
				adminActions(s);
					break;
					
			} catch (NumberFormatException e) {
				logger.info("NumberFormatException at Applicant Approval; Invalid Entry");				
				adminDashboard(s);
				adminActions(s);
				break;
			} catch (NullPointerException e) {
				logger.info("NullPointerException at Applicant Approval; No Applicant with Such Identifier");
				adminDashboard(s);
				adminActions(s);
				break;
			}
			
		case "6": 		
			// APPROVE JOINT ACCOUNTS ( X )
			System.out.println("Please select a joint account application on which to operate: ");
			System.out.println(aDao.getJointAccountApplications());
			String jointnameSelection = scan.nextLine();
			System.out.println("Name | Customer 1 | Customer 2");

			System.out.println(aDao.retrieveJointInfo(jointnameSelection));
			System.out.println("Approve this joint account?");
			System.out.println("Press 1 to approve.");
			System.out.println("Press 2 to reject.");
			String approvalSelection = scan.nextLine();
			
			
			switch(approvalSelection) {
			case "1":
	
				
				aDao.initializeAccountBalance(jointnameSelection);
				System.out.println("Account Approved!");
				adminDashboard(s);
				adminActions(s);
				break;
				
			case "2":
				aDao.deleteJointAccountApplication(jointnameSelection);
				System.out.println("Account Deleted.");
				adminDashboard(s);
				adminActions(s);
				break;
				
			default:
				adminDashboard(s);
				adminActions(s);
				break;
			}
			
			
		case "7":
			// CREATE EMPLOYEE AND ADMIN ACCOUNTS ( X )
			System.out.println("Please indicate which account type you would like to create:");
			System.out.println("Press 1 for Employee");
			System.out.println("Press 2 for Administrator");
			String staffAdditionSelection = scan.nextLine();
			if (staffAdditionSelection.equals("1")) {
				System.out.println("Creating an Employee Account");
				aDao.applyForEmployeeAccount();
				adminDashboard(s);
				adminActions(s);
				break;
			}else if (staffAdditionSelection.equals("2")) {
				System.out.println("Creating an Administrator Account");
				aDao.applyForAdminAccount();
				
				adminDashboard(s);
				adminActions(s);
				break;
			
			} else if (staffAdditionSelection.equals("9")) {
				System.out.println("Staff account addition aborted.");
				adminDashboard(s);
				adminActions(s);
				break;
			}
			
			adminDashboard(s);
			adminActions(s);
			break;
			
		// DELETE ACCOUNTS ( X )
		case "8":
			System.out.println("Currently "+ customertotalstring +" Account(s)");
			
		 try {
				if (customertotalint == 0) {
					System.out.println("There are no customers at this time! Operation terminated(1)...");
					adminDashboard(s);
					adminActions(s);
					break;
				}
				System.out.println(aDao.getCustomerUsernames());
			System.out.println("\n Please select a user whose account you'd like to close:");
			System.out.println("\n Press 9 to exit.");
			String accountToClose = scan.nextLine();
			if (accountToClose.equals("9")) {
				System.out.println("Account cancelation aborted.");
				adminDashboard(s);
				adminActions(s);
				break;
			}
	
			aDao.deleteCustomerRecords(accountToClose);
			aDao.deleteFinancialRecords(accountToClose);
			
			} catch (NumberFormatException e) {
				logger.info("NumberFormatException at Applicant Approval; Invalid Entry");				
				adminDashboard(s);
				adminActions(s);
				break;
			} catch (NullPointerException e) {
				logger.info("NullPointerException at Applicant Approval; No Applicant with Such Identifier");
				adminDashboard(s);
				adminActions(s);
				break;
			}
			adminDashboard(s);
			adminActions(s);
			break;
			
		case "9":
			landingPage();
			MainMenuScreen.mainMenu();
			break;

		default:
			adminDashboard(s);
			adminActions(s);
			break;
		}
	}
}