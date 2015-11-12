package ad.uda.moro.ejb.Exerciser;

import java.util.Properties;
import java.util.Scanner;

import javax.naming.*;

import ad.uda.moro.ejb.entity.ActivitatDossier;
import ad.uda.moro.ejb.entity.Dossier;
import ad.uda.moro.ejb.entity.Servei;
import ad.uda.moro.ejb.session.EnquestesServiceRemote;
import ad.uda.moro.CommonUtilities;
import ad.uda.moro.MoroException;

public class MinimumExerciser {

	// Remoting context:
	InitialContext initialContext = null;

	private static final String PKG_INTERFACES = "org.jboss.ejb.client.naming";

	private EnquestesServiceRemote enquestaService = null;

	private Scanner scanner;

	public static void main(String[] args) {
		new MinimumExerciser().exercise();
	}

	private void exercise() {
		System.out.println("EXERCISER STARTS HERE");

		// Create InitialContext:
		Properties properties = new Properties();
		properties.put(Context.URL_PKG_PREFIXES, PKG_INTERFACES);

		try {
			this.initialContext = new InitialContext(properties);
			System.out.println("InitialContext created");
		} catch (NamingException ex) {
			System.out.println("Create InitialContext - ERROR. Details: " + ex.getMessage());
		}

		// Look up EnquestesServiceRemote:
		String lookupName = CommonUtilities.getLookupEJBName("EnquestesServiceBean",
				EnquestesServiceRemote.class.getName());
		System.out.println("About to look up EnquestesServiceRemote. JNDI name = [" + lookupName + "]");
		try {
			this.enquestaService = (EnquestesServiceRemote) initialContext.lookup(lookupName);
			System.out.println("Look up EnquestesServiceRemote succeeded");
		} catch (NamingException ex) {
			System.out.println("Look up EnquestesServiceRemote ERROR. Details: " + ex.getMessage());
		}

		menu();
		System.out.println("EXERCISER ENDS HERE");

	}

	private void menu() {
		int choice;
		do {
			System.out.println("============================");
			System.out.println("|      MORO EXERCISER      |");
			System.out.println("============================");
			System.out.println("| Options:                 |");
			System.out.println("| 0. Exit                  |");
			System.out.println("| 1. CRUD ActivitatDossier |");
			System.out.println("| 2. Llistats              |");
			System.out.println("============================");

			scanner = new Scanner(System.in);
			choice = (int) scanner.nextInt();

			switch (choice) {
			case 0:
				System.out.println("Exiting...");
				break;
			case 1:
				crudActivitatDossier();
				break;
			case 2:
				menuList();
				break;
			default:
				System.out.println("Invalid selection");
				break;
			}
		} while (choice != 0);
	}

	private void crudActivitatDossier() {
		int choice;
		do {
			System.out.println("============================");
			System.out.println("|      MORO EXERCISER      |");
			System.out.println("============================");
			System.out.println("| CRUD activitatDossier:   |");
			System.out.println("| 0. Exit                  |");
			System.out.println("| 1. Inserir item          |");
			System.out.println("| 2. Eliminar item         |");
			System.out.println("| 3. Modificar item        |");
			System.out.println("| 4. Llistar items         |");
			System.out.println("============================");

			scanner = new Scanner(System.in);
			choice = (int) scanner.nextInt();

			switch (choice) {
			case 0:
				System.out.println("Exiting...");
				break;
			case 1:
				addActivitatDossier();
				break;
			case 2:
				deleteActivitatDossier();
				break;
			case 3:
				updateActivitatDossier();
				break;
			case 4:
				activitatDossierList();
				break;
			default:
				System.out.println("Invalid selection");
				break;
			}
		} while (choice != 0);
	}

	private void addActivitatDossier() {
		scanner = new Scanner(System.in);

		System.out.println("Entra el idDossier:");
		int idDossier = (int) scanner.nextInt();

		Dossier dossier = null;
		try {
			dossier = enquestaService.getDossierById(idDossier);
		} catch (MoroException ex) {
			System.out.println("ERROR dossier: " + ex.getMessage());
		}

		System.out.println("Entra el idServei:");
		int idServei = (int) scanner.nextInt();
		Servei servei = null;
		try {
			servei = enquestaService.getServeiById(idServei);
		} catch (MoroException ex) {
			System.out.println("ERROR servei: " + ex.getMessage());
		}
		

		ActivitatDossier a = new ActivitatDossier(dossier, servei);
		int id = a.getId();
		try {
			this.enquestaService.addActivitatDossier(a);
			a = this.enquestaService.getActivitatDossier(id);
			if (a == null) {
				System.out.println("ActivitatDossier with code [" + id + "] not added succesfully.");
			}
			System.out.println("ActivitatDossier succesfully added. Details:" + a.toString());
		} catch (MoroException ex) {
			System.out.println("ERROR: " + ex.getMessage());
		}
	}

	private void deleteActivitatDossier() {
		activitatDossierList();
		scanner = new Scanner(System.in);

		System.out.println("Entra el id del item que vols eliminar:");
		int id = (int) scanner.nextInt();

		try {
			ActivitatDossier a = enquestaService.getActivitatDossier(id);
			if (a == null) {
				System.out.println("ActivitatDossier with ID [" + id + "] not found");
			}
			enquestaService.deleteActivitatDossier(id);
			System.out.println("ActivitatDossier with ID [" + id + "] deleted");
		} catch (MoroException ex) {
			System.out.println("ERROR: " + ex.getMessage());
		}
	}

	private void updateActivitatDossier() {
//		activitatDossierList();
//		scanner = new Scanner(System.in);
//
//		System.out.println("Entra el id del item que vols modificar:");
//		int id = (int) scanner.nextInt();
//
//		try {
//			ActivitatDossier a = enquestaService.getActivitatDossier(id);
//			if (a == null) {
//				System.out.println("ActivitatDossier with ID [" + id + "] not found");
//			} else {
//				System.out.println("Entra el nou idDossier:");
//				int newIdDossier = (int) scanner.nextInt();
//
//				System.out.println("Entra el nou idServei:");
//				int newIdServei = (int) scanner.nextInt();
//
//				a.setIdDossier(newIdDossier);
//				a.setIdServei(newIdServei);
//
//				try {
//					enquestaService.updateActivitatDossier(a);
//					System.out.println("ActivitatDossier with ID [" + id + "] updated");
//				} catch (MoroException ex) {
//					System.out.println("ERROR: " + ex.getMessage());
//				}
//			}
//		} catch (MoroException ex) {
//			System.out.println("ERROR: " + ex.getMessage());
//		}
	}

	private void activitatDossierList() {
		try {
			ActivitatDossier[] a = this.enquestaService.getActivitatDossierList();
			for (int i = 0; i < a.length; i++) {
				System.out.println(a[i].toString());
			}
		} catch (Exception ex) {
			System.out.println("EJB access to getActivitatDossierList failed. Reason: " + ex.getMessage());
		}
	}

	private void menuList() {
		int choice;
		do {
			System.out.println("============================");
			System.out.println("|      MORO EXERCISER      |");
			System.out.println("============================");
			System.out.println("| Llistats:                |");
			System.out.println("| 0. Exit                  |");
			System.out.println("| 1. ServeiDossier         |");
			System.out.println("| 2. ValoracioServei       |");
			System.out.println("| 3. ValoracioParametre    |");
			System.out.println("============================");

			scanner = new Scanner(System.in);
			choice = (int) scanner.nextInt();

			switch (choice) {
			case 0:
				System.out.println("Exiting...");
				break;
			case 1:
				serveisDossierList();
				break;
			case 2:
				valoracioServeiList();
				break;
			case 3:
				valoracioParametreList();
				break;
			default:
				System.out.println("Invalid selection");
				break;
			}
		} while (choice != 0);
	}
	
	private void serveisDossierList() {
		activitatDossierList();
		
		scanner = new Scanner(System.in);
		System.out.println("De quin Dossier vols llistar els serveis ?(entrar idDossier)");
		int id = (int) scanner.nextInt();
		
		try {
			Servei[] s = this.enquestaService.getServeisDossierList(id);
			for (int i = 0; i < s.length; i++) {
				System.out.println(s[i].toString());
			}
		} catch (Exception ex) {
			System.out.println("EJB access to getActivitatDossierList failed. Reason: " + ex.getMessage());
		}
		
	}
	
	private void valoracioServeiList() {
		
		
	}

	private void valoracioParametreList() {
		
		
	}
	
}
