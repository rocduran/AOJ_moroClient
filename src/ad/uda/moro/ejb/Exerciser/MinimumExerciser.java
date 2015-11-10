package ad.uda.moro.ejb.Exerciser;

import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

import javax.naming.*;

import ad.uda.moro.ejb.entity.ActivitatDossier;
import ad.uda.moro.ejb.session.EnquestesServiceRemote;

import ad.uda.moro.CommonUtilities;

public class MinimumExerciser {

	//Remoting context:
	InitialContext initialContext = null;
	private static final String PKG_INTERFACES = "org.jboss.ejb.client.naming";
	EnquestesServiceRemote enquestaService = null;
		
		

	public static void main(String[] args) {
		new MinimumExerciser().exercise();
	}
	
	private void exercise(){
		System.out.println("EXERCISER STARTS HERE");
		
		//Create InitialContext:
		Properties properties = new Properties();
		properties.put(Context.URL_PKG_PREFIXES, PKG_INTERFACES);
		
		try {
			this.initialContext = new InitialContext(properties);
			System.out.println("InitialContext created");
		} catch (NamingException ex) {
			System.out.println("Create InitialContext - ERROR. Details: " + ex.getMessage());
		}
		
		//Look up AquariumServiceRemote:
		String lookupName = CommonUtilities.getLookupEJBName("EnquestesServiceBean", EnquestesServiceRemote.class.getName());
		System.out.println("About to look up EnquestesServiceRemote. JNDI name = [" + lookupName + "]");
		try {
			this.enquestaService = (EnquestesServiceRemote)initialContext.lookup(lookupName);
			System.out.println("Look up EnquestesServiceRemote succeeded");
		} catch (NamingException ex) {
			System.out.println("Look up EnquestesServiceRemote ERROR. Details: " + ex.getMessage());
		}
		
		
		
		// Test at least one business method to see if EJB access really works:
		System.out.println("Trying out the business logic...");
		try {
			ActivitatDossier a = this.enquestaService.getActivitatDossier(3);
			System.out.println("getActivity(3)succeded:" + a.toString() + "])");
		} catch (Exception ex) {
			System.out.println("EJB access to EnquestesServiceRemote failed. Reason: " + ex.getMessage());
		}
	    
		System.out.println("EXERCISER ENDS HERE");
		
		menu();
	}
	
	private void menu(){
		int choice;
		do{
			System.out.println("============================");
			System.out.println("|      MORO EXERCISER      |");
			System.out.println("============================");
			System.out.println("| Options:                 |");
			System.out.println("| 1. CRUD ActivitatDossier |");
			System.out.println("| 2. Llistats              |");
			System.out.println("| 3. Exit                  |");
			System.out.println("============================");
			
			Scanner scanner = new Scanner(System.in);
		    choice = (int) scanner.nextInt();
	
			// Switch construct
			switch (choice) {
			case 1:
			  System.out.println("Option 1 selected");   // This is where I want to call the class
			  break;
			case 2:
			  System.out.println("Option 2 selected");  // this is where I want to call the class
			  break;
			case 3:
			  System.out.println("Exit selected");
			  break;
			default:
			  System.out.println("Invalid selection");
			  break; // This break is not really necessary
			}
		}while (choice != 3);
	}
}
