package autodidactics.dao;

import java.util.Map;

import autodidactics.models.Customer;

public interface AdminDao extends CustomerDao, EmployeeDao {
	
	public void cancelAccount(String usernameToClose, Map<String, Customer> customerPool);

}