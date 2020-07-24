package autodidactics.dao;

public class LinkageDao {
	
	private final static String dbUrl = "$NEWCHRISPYBANKDB_URL";
	private final static String dbUsername = System.getenv("$NEWCHRISPYBANKDB_USERNAME");
	private final static String dbPassword = System.getenv("$NEWCHRISPYBANKDB_PASSWORD");
	
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
