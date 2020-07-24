package autodidactics.dao;

import java.util.List;
import java.util.Map;

import autodidactics.models.Customer;

public interface EmployeeDao {
	
	public List<String> retrieveCustomerInfo(String username);
	List<String> getApplicantUsernames();
}
