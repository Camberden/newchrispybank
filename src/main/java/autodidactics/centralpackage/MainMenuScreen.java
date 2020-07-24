package autodidactics.centralpackage;

public class MainMenuScreen extends LoginScreens implements Scannable {
	
	public static void mainMenu () {
		
		landingPage();
	
		String initialInput = scan.nextLine();
		switch (initialInput) {
		
		case "1": {
			customerLoginScreen();			
			mainMenu();

			break;
		}
		case "2": {
			applyForAccount();
			mainMenu();

			break;
		}
		case "3": {
			staffLoginScreen();
			mainMenu();

			break;
		}
		case "4": {
			staffLoginScreen();
			mainMenu();

		}
		case "5": {
			terminateSession();
		
		}
		case "10": {		
		}
		default:
			System.out.println("Invalid Input! Please try again.");
			mainMenu();
			break;
			
		}
	}
}


