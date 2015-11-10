package ad.uda.moro.ejb.Exerciser;

import java.util.Properties;

import javax.naming.*;

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
		
		//Test business method:
		System.out.println("Trying business logic..");
		try {
			String test = this.enquestaService.helloWorld();
			System.out.println("EnquestesServiceRemote.helloWorld() return: [" + test + "]");
		} catch (Exception e) {
			System.out.println("EJB access to EnquestesServiceRemoce failed. Details: " + e.getMessage());
		}
		
		
		System.out.println("EXERCISER ENDS HERE. Good Bye");
	}

}
