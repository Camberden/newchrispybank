package autodidactics.centralpackage;

import autodidactics.models.Customer;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import autodidactics.centralpackage.LoginScreens;
import autodidactics.dao.EmployeeDao;
import autodidactics.dao.EmployeeDaoImpl;
import autodidactics.dao.LinkageDao;

public class EmployeeService extends LoginScreens implements Scannable {
	
	protected static void employeeActions(String s) {
		
		final Logger logger = Logger.getLogger(EmployeeService.class);

		
		EmployeeDaoImpl eDao = new EmployeeDaoImpl();
		List<String> customertotal = eDao.getCustomerTotal();
		String customertotalstring = customertotal.get(0);
		int customertotalint = Integer.parseInt(customertotalstring);
		
		String dashboardSelection = scan.nextLine();
		switch (dashboardSelection) {

		case "1":
			
			if (customertotalint == 0) {
				System.out.println("There are no customers at this time! Operation terminated(1)...");
				employeeDashboard(s);
				employeeActions(s);
				break;
			}
			System.out.println("Customer Data:\n");
			System.out.println("Select a customer to present his/her details:");
			System.out.println(eDao.getCustomerUsernames());
			String detailsSelection = scan.nextLine();
			System.out.println("Username | Name | Email");
			System.out.println(eDao.retrieveCustomerInfo(detailsSelection));
			String result = String.join(",", eDao.retriveCustomerBalance(detailsSelection));
			double currentFunds = Double.parseDouble(result);
			System.out.println(detailsSelection + "'s current balance is $" + currentFunds);
		
			employeeDashboard(s);
			employeeActions(s);
			break;	
			
	
			
			
			
			
		case "2":
			// APPLICANT APPROVAL ( )
			
						List<String> yieldlist = eDao.getApplicantTotal();
						String yieldstring = yieldlist.get(0);
						int yieldint = Integer.parseInt(yieldstring);
						System.out.println("Currently "+ yieldstring + " Account(s) Requested\n");
						
						try {
							if (yieldint == 0) {
								System.out.println("There are no applicants at this time! Operation terminated(1)...");
								employeeDashboard(s);
								employeeActions(s);
								break;
							}
							
							System.out.println("Please choose the username corresponding to the "
									+ "Application on which you'd like to operate:");
							System.out.println(eDao.getApplicantUsernames());
							String applicantToProcess = scan.nextLine();
							
							System.out.println("Approve applicant " + applicantToProcess + "?");
							System.out.println("Press 1 to Approve.");
							System.out.println("Press 2 to Reject.");
							String decision = scan.nextLine();
							
							switch (decision) {
							case "1":
								eDao.approveCustomerBeforeApplicationDeletion(applicantToProcess);
								eDao.initializeAccountBalance(applicantToProcess);
								eDao.deleteApplicantRecords(applicantToProcess);
								System.out.println("Account Approved!");
								break;
							case "2":
								eDao.deleteApplicantRecords(applicantToProcess);
								System.out.println("Account Request Denied!");
								break;
							
							default:
								System.out.println(("Invalid Input. Operation Terminated..."));
								employeeDashboard(s);
								employeeActions(s);
								break;
								
							}
								
							employeeDashboard(s);
							employeeActions(s);
								break;
								
						} catch (NumberFormatException e) {
							logger.info("NumberFormatException at Applicant Approval; Invalid Entry");
							employeeDashboard(s);
							employeeActions(s);
							
							break;
						} catch (NullPointerException e) {
							logger.info("NullPointer at Applicant Approval; No Applicant with Such Identifier");

							employeeDashboard(s);
							employeeActions(s);
							break;
						}
					
		case "3":
				landingPage();
				MainMenuScreen.mainMenu();
				
				break;
			
			default:
				employeeDashboard(s);
				employeeActions(s);
				break;
		}
	}
}
