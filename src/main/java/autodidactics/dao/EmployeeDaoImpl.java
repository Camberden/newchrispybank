package autodidactics.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.postgresql.util.PSQLException;

import autodidactics.centralpackage.CustomerService;
import autodidactics.centralpackage.Scannable;
import autodidactics.models.Customer;
import autodidactics.dao.LinkageDao;

public class EmployeeDaoImpl implements EmployeeDao, Scannable {
	
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
//					valueList.add(rs.getString(4));
					// EMPLOYEES MAY NOT SEE THE 4TH COLUMN (PASSWORD)
				}
//				ps.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			return valueList;
		}
	
	public void insertCustomer(Customer customer) {
		LinkageDao l = new LinkageDao();
		try {
			Connection conn = DriverManager.getConnection(l.getDbUrl(), l.getDbUsername(), l.getDbPassword());
			String sql = "{ ? = call customer_insert(?,?,?,?)}";
			String sql2 = "{ ? = call make_balance_table(?, 0.0)}";
			CallableStatement cs = conn.prepareCall(sql);
			CallableStatement cs2 = conn.prepareCall(sql2);

		cs.registerOutParameter(1, Types.VARCHAR);
		cs.setString(2, customer.getUsername());
		cs.setString(3, customer.getName());
		cs.setString(4, customer.getEmail());
		cs.setString(5, customer.getPassword());
		cs2.registerOutParameter(1, Types.VARCHAR);
		cs2.setString(2, customer.getUsername());
		
		cs.execute();
		cs2.execute();
		System.out.println(cs.getString(1));
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
//			ps.execute();
		} catch (SQLException e) {
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
			e.printStackTrace();
		}
		return valueList;
 	}
	
	//  SELECT * FROM applicants where username = ?

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
			} catch (PSQLException e) {
			System.out.println("=== Information Updated === ");;
			} catch (SQLException e) {
			e.printStackTrace();
		
			}
		return valueList;
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
			System.out.println(" === Information Updated === ");
			} catch (SQLException e) {
			e.printStackTrace();
		
			}
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
			e.printStackTrace();
		}
		
		return newCustomer;
		
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
	//		ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return valueList;
	}

	public List<String> retriveCustomerBalance(String selection) {
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
	
	public void approveCustomerBeforeApplicationDeletion(String selection) {
			
			LinkageDao l = new LinkageDao();
			Connection conn;
			Customer newCustomer = new Customer();
			
			try {
				conn = DriverManager.getConnection(l.getDbUrl(), l.getDbUsername(), l.getDbPassword());
				String sql = "INSERT INTO customers(SELECT username, name, email, password FROM applicants WHERE username = ?)";
				PreparedStatement ps = conn.prepareStatement(sql);
				
				ps.setString(1,  selection);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					newCustomer = new Customer (rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
				}
			} catch (PSQLException e) {
				System.out.println(" === INFORMATION UPDATED === ");
			} catch (SQLException e) {
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
			System.out.println("=== INFORMATION UPDATED ===");;
		} catch (SQLException e) {
			e.printStackTrace();
		}
}
	
}



// NOTES ---------------------------------------------------------------------------------
