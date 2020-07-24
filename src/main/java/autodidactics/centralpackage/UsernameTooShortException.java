package autodidactics.centralpackage;

@SuppressWarnings("serial")
public class UsernameTooShortException extends Exception { 
	
    public UsernameTooShortException(String errorMessage) {
        super(errorMessage);
    }
}