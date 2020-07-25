package autodidactics.dao;

public class LinkageDao {
	
//	private final static String dbUrl = "jdbc:postgresql://camberdb2.ckxhjl2whzby.us-east-2.rds.amazonaws.com:5432/newchrispybankdb";
	private final static String dbUrl = System.getenv("NEWCHRISPYBANKDB_URL");
	private final static String dbUsername = System.getenv("NEWCHRISPYBANKDB_USERNAME");
	private final static String dbPassword = System.getenv("NEWCHRISPYBANKDB_PASSWORD");
	
	public String getDbUrl() {
		return dbUrl;
	}

	public String getDbUsername() {
		return dbUsername;
	}

	public String getDbPassword() {
		return dbPassword;
	}
	
}
