package autodidactics.dao;

import java.util.List;

import autodidactics.models.Customer;

public interface CustomerDao {
	
	void doDatabaseDepositOrWithdraw(String actor, double balanceAfterAction);
}
