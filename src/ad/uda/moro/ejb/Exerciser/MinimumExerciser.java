package ad.uda.moro.ejb.Exerciser;

import java.util.Properties;
import java.util.Scanner;

import javax.naming.*;

import ad.uda.moro.ejb.entity.ActivitatDossier;
import ad.uda.moro.ejb.entity.Dossier;
import ad.uda.moro.ejb.entity.Parametre;
import ad.uda.moro.ejb.entity.Servei;
import ad.uda.moro.ejb.entity.Valoracio;
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
			System.out.println("| 2. CRUD Dossier          |");
			System.out.println("| 3. CRUD Servei           |");
			System.out.println("| 4. CRUD Parametre        |");
			System.out.println("| 5. CRUD Valoracio        |");
			System.out.println("| 6. Llistats              |");
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
				crudDossier();
				break;
			case 3:
				crudServei();
				break;
			case 4:
				crudParametre();
				break;
			case 5:
				crudValoracio();
				break;
			case 6:
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
		try {
			this.enquestaService.addActivitatDossier(a);
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
			ActivitatDossier a = enquestaService.getActivitatDossierById(id);
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
		activitatDossierList();
		scanner = new Scanner(System.in);

		System.out.println("Entra el id del item que vols modificar:");
		int id = (int) scanner.nextInt();
		
		ActivitatDossier activitatDossier = null;
		try {
			activitatDossier = enquestaService.getActivitatDossierById(id);
			if (activitatDossier == null){
				System.out.println("activitatDossier with id=" + id + " is null !");
				return;
			}
		} catch (Exception ex) {
			System.out.println("ERROR getting ActivitatDossier: " + ex.getMessage());
			return;
		}
		
		scanner = new Scanner(System.in);
		System.out.println("Entra el nou idDossier:");
		int idDossier = (int) scanner.nextInt();
		
		Dossier newDossier = null;
		try {
			newDossier = enquestaService.getDossierById(idDossier);
			if (newDossier == null){
				System.out.println("Dossier with id=" + idDossier + " is null !");
				return;
			}
		} catch (Exception ex) {
			System.out.println("ERROR getting Dossier: " + ex.getMessage());
			return;
		}
		
		scanner = new Scanner(System.in);
		System.out.println("Entra el nou idServei:");
		int idServei = (int) scanner.nextInt();
		
		Servei newServei = null;
		try {
			newServei = enquestaService.getServeiById(idServei);
			if (newServei == null){
				System.out.println("Servei with id=" + idServei + " is null !");
				return;
			}
		} catch (Exception ex) {
			System.out.println("ERROR getting Servei: " + ex.getMessage());
			return;
		}
		
		activitatDossier.setIdDossier(newDossier);
		activitatDossier.setIdServei(newServei);
		
		try {
			enquestaService.updateActivitatDossier(activitatDossier);
			System.out.println("ActivitatDossier updated succesfully !");
		} catch (Exception ex) {
			System.out.println("ERROR updating ActivitatDossier: " + ex.getMessage());
		}
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

	private void crudDossier() {
		int choice;
		do {
			System.out.println("============================");
			System.out.println("|      MORO EXERCISER      |");
			System.out.println("============================");
			System.out.println("| CRUD Dossier:            |");
			System.out.println("| 0. Exit                  |");
			System.out.println("| 1. Inserir item          |");
			System.out.println("| 2. Eliminar item         |");
			System.out.println("| 3. Modificar item        |");
			System.out.println("============================");

			scanner = new Scanner(System.in);
			choice = (int) scanner.nextInt();

			switch (choice) {
			case 0:
				System.out.println("Exiting...");
				break;
			case 1:
				addDossier();
				break;
			case 2:
				deleteDossier();
				break;
			case 3:
				updateDossier();
				break;
			default:
				System.out.println("Invalid selection");
				break;
			}
		} while (choice != 0);
	}
	
	private void addDossier() {
		scanner = new Scanner(System.in);

		System.out.println("Entra el preu:");
		int preu = (int) scanner.nextInt();
		
		scanner = new Scanner(System.in);
		System.out.println("Entra la descripcio:");
		String desc = (String) scanner.nextLine();
			
		Dossier dossier = new Dossier(preu, desc);

		try {
			this.enquestaService.addDossier(dossier);
			System.out.println("Dossier succesfully added.");
		} catch (MoroException ex) {
			System.out.println("ERROR: " + ex.getMessage());
		}
	}

	private void deleteDossier() {
		// TODO Auto-generated method stub
		
	}

	private void updateDossier() {
		// TODO Auto-generated method stub
		
	}

	private void crudServei() {
		int choice;
		do {
			System.out.println("============================");
			System.out.println("|      MORO EXERCISER      |");
			System.out.println("============================");
			System.out.println("| CRUD Servei:             |");
			System.out.println("| 0. Exit                  |");
			System.out.println("| 1. Inserir item          |");
			System.out.println("| 2. Eliminar item         |");
			System.out.println("| 3. Modificar item        |");
			System.out.println("============================");

			scanner = new Scanner(System.in);
			choice = (int) scanner.nextInt();

			switch (choice) {
			case 0:
				System.out.println("Exiting...");
				break;
			case 1:
				addServei();
				break;
			case 2:
				deleteServei();
				break;
			case 3:
				updateServei();
				break;
			default:
				System.out.println("Invalid selection");
				break;
			}
		} while (choice != 0);
	}

	private void addServei() {
		scanner = new Scanner(System.in);

		System.out.println("Entra el idTipus:");
		int idTipus = (int) scanner.nextInt();
		
		scanner = new Scanner(System.in);
		System.out.println("Entra la descripcio:");
		String desc = (String) scanner.nextLine();
			
		Servei servei = new Servei(idTipus, desc);

		try {
			this.enquestaService.addServei(servei);
			System.out.println("Servei succesfully added.");
		} catch (MoroException ex) {
			System.out.println("ERROR: " + ex.getMessage());
		}
		
	}

	private void deleteServei() {
		// TODO Auto-generated method stub
		
	}

	private void updateServei() {
		// TODO Auto-generated method stub
		
	}

	private void crudParametre() {
		int choice;
		do {
			System.out.println("============================");
			System.out.println("|      MORO EXERCISER      |");
			System.out.println("============================");
			System.out.println("| CRUD Parametre:          |");
			System.out.println("| 0. Exit                  |");
			System.out.println("| 1. Inserir item          |");
			System.out.println("| 2. Eliminar item         |");
			System.out.println("| 3. Modificar item        |");
			System.out.println("============================");

			scanner = new Scanner(System.in);
			choice = (int) scanner.nextInt();

			switch (choice) {
			case 0:
				System.out.println("Exiting...");
				break;
			case 1:
				addParametre();
				break;
			case 2:
				deleteParametre();
				break;
			case 3:
				updateParametre();
				break;
			default:
				System.out.println("Invalid selection");
				break;
			}
		} while (choice != 0);
	}
		
	private void addParametre() {
		scanner = new Scanner(System.in);

		System.out.println("Entra el idTipus:");
		int preu = (int) scanner.nextInt();
		
		scanner = new Scanner(System.in);
		System.out.println("Entra la descripcio:");
		String desc = (String) scanner.nextLine();
			
		Parametre parametre = new Parametre(preu, desc);

		try {
			this.enquestaService.addParametre(parametre);
			System.out.println("Parametre succesfully added.");
		} catch (MoroException ex) {
			System.out.println("ERROR: " + ex.getMessage());
		}
	}

	private void deleteParametre() {
		// TODO Auto-generated method stub
		
	}

	private void updateParametre() {
		// TODO Auto-generated method stub
		
	}

	private void crudValoracio() {
		int choice;
		do {
			System.out.println("============================");
			System.out.println("|      MORO EXERCISER      |");
			System.out.println("============================");
			System.out.println("| CRUD Valoracio:          |");
			System.out.println("| 0. Exit                  |");
			System.out.println("| 1. Inserir item          |");
			System.out.println("| 2. Eliminar item         |");
			System.out.println("| 3. Modificar item        |");
			System.out.println("============================");

			scanner = new Scanner(System.in);
			choice = (int) scanner.nextInt();

			switch (choice) {
			case 0:
				System.out.println("Exiting...");
				break;
			case 1:
				addValoracio();
				break;
			case 2:
				deleteValoracio();
				break;
			case 3:
				updateValoracio();
				break;
			default:
				System.out.println("Invalid selection");
				break;
			}
		} while (choice != 0);
	}
	
	private void addValoracio() {
		scanner = new Scanner(System.in);

		System.out.println("Entra el idDossier:");
		int idDossier = (int) scanner.nextInt();
		Dossier dossier = null;
		try {
			dossier = enquestaService.getDossierById(idDossier);
		} catch (Exception e) {
			System.out.println("Error while looking at Dossier with id: " + idDossier);
			return;
		}
		
		System.out.println("Entra el idServei:");
		int idServei = (int) scanner.nextInt();
		Servei servei = null;
		try {
			servei = enquestaService.getServeiById(idServei);
		} catch (Exception e) {
			System.out.println("Error while looking at Servei with id: " + idServei);
			return;
		}
		
		System.out.println("Entra el idParam:");
		int idParam = (int) scanner.nextInt();
		Parametre parametre = null;
		try {
			parametre = enquestaService.getParametreById(idParam);
		} catch (Exception e) {
			System.out.println("Error while looking at Parametre with id: " + idParam);
			return;
		}
		
		System.out.println("Entra el valor:");
		int valor = (int) scanner.nextInt();
		
		Valoracio valoracio = new Valoracio(dossier, servei, parametre, valor);

		try {
			this.enquestaService.addValoracio(valoracio);
			System.out.println("Valoracio succesfully added.");
		} catch (MoroException ex) {
			System.out.println("ERROR: " + ex.getMessage());
		}
		
	}

	private void deleteValoracio() {
		// TODO Auto-generated method stub
		
	}

	private void updateValoracio() {
		// TODO Auto-generated method stub
		
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
		dossierList();
		
		scanner = new Scanner(System.in);
		System.out.println("De quin Dossier vols llistar els serveis ?(entrar idDossier)");
		int id = (int) scanner.nextInt();
		
		try {
			ActivitatDossier[] a = this.enquestaService.getServeisDossierList(id);
			for (int i = 0; i < a.length; i++) {
				System.out.println(a[i].getIdServei());
			}
		} catch (Exception ex) {
			System.out.println("EJB access to getActivitatDossierList failed. Reason: " + ex.getMessage());
		}
		
	}
	
	private void dossierList() {
		try {
			Dossier[] dossiers = this.enquestaService.getDossierList();
			for (int i = 0; i < dossiers.length; i++) {
				System.out.println(dossiers[i].toString());
			}
		} catch (Exception ex) {
			System.out.println("EJB access to getActivitatDossierList failed. Reason: " + ex.getMessage());
		}
		
	}
	
	private void serveiList(){
		try {
			Servei[] serveis = this.enquestaService.getServeiList();
			for (int i = 0; i < serveis.length; i++) {
				System.out.println(serveis[i].toString());
			}
		} catch (Exception ex) {
			System.out.println("EJB access to getActivitatDossierList failed. Reason: " + ex.getMessage());
		}
	}
	
	private void valoracioServeiList() {
		serveiList();
		
		scanner = new Scanner(System.in);
		System.out.println("Entra el id del Servei que vulguis llistar les valoracions:");
		int idServei = (int) scanner.nextInt();
		
		Servei servei = null;
		try {
			servei = enquestaService.getServeiById(idServei);
			if (servei == null){
				System.out.println("Dossier with id= " + idServei + "is null!");
				return;
			}
		} catch (Exception ex) {
			System.out.println("EJB access to getServeiById failed. Reason: " + ex.getMessage());
		}
		
		try {
			Valoracio[] valoracions = this.enquestaService.getValoracioServei(idServei);
			for (int i = 0; i < valoracions.length; i++) {
				System.out.println(valoracions[i].getIdServei());
			}
		} catch (Exception ex) {
			System.out.println("EJB access to getValoracioServei failed. Reason: " + ex.getMessage());
		}
		
		
		
	}

	private void valoracioParametreList() {
		//TODO implement
		
	}

}
