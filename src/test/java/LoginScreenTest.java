import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import autodidactics.centralpackage.*;
import autodidactics.models.Customer;

public class LoginScreenTest {
	
	static Customer testCustomer;

	
	@BeforeClass
	public static void setUpBeforeClass() {
		System.out.println(" === BEFORE THE CLASS === ");
		
	}
	
	@AfterClass
	public static void tearDownAfterClass() {
		System.out.println(" === AFTER THE CLASS === ");
		
	}
	
	@Before
	public void setUp() { 
		System.out.println(" === BEFORE THE METHOD === ");

	}
	
	@After
	public void tearDown() {
		System.out.println(" === AFTER THE METHOD === ");

	}
	
	

}
