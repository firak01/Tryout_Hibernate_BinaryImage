package tryout.hibernate;

import javax.persistence.EntityManager;

import org.hibernate.Session;

public class TryoOutHexCellGenerateMain {

	/**Basierend auf dem Entity:
	 * Erzeuge in der Tabelle zum einen die Spalten aus der eingebetteten Schlüssel-Klasse.
	 * Dort sind die Werte als String vorhanden.
	 * 
	 * Erzeuge aber aber auch eine Spalte mit den Werten als Integer und persitiere diese.
	 * Damit kann man dann im HQL beweisen, dass MAX unterschiedliche Ergebnisse liefert für STRING oder INTEGER.
	 * 
	 * Desweiteren Kann man im HQL beweisen, dass es einen Unterschied macht, ob der ACCESSTYPE für FIELD oder PROPERTY in den Entities 
	 * (oder hier der Schlüsselklasse)eingestellt ist.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		HibernateContextProvider objContextHibernate = new HibernateContextProvider();
		
		
		//Erzeuge den Entity Manager als Ausgangspunkt für die Abfragen. !!! Damit Hibernate mit JPA funktioniert, braucht man die Datei META-INF\persistence.xml. Darin wird die persistence-unit angegeben.		
		EntityManager em = objContextHibernate.getEntityManager("TryoutHibernateBinaryImage001");
		
		
		/**
		 * Merke: Code um ein Image als Blob in die Datenbank zu schreiben.
		 * 1. Siehe UIHelperDummy, um mit dem InputStream zu arbeiten
		 * ...
		 * Buch.. Seite 130ff.
		 */
		
		/*++++++++++++++*/
		//Erzeugen der Entities		
		Session session = objContextHibernate.getSession();
		
		//Vorbereiten der Wertübergabe an die Datenbank
		session.beginTransaction();
		
		
		//Zwei verschachtelte Schleifen, Aussen: Solange wie es "Provinzen" gibt...
		//                                                  Innen:   von 1 bis maximaleSpaltenanzahl...
		int iY = 0;
		do{//die maximale Zeilenzahl 
			iY++;
			Integer intY = new Integer(iY);
			String sY = intY.toString();
			
			for(int iX=1; iX <= 10; iX++){
				Integer intX = new Integer(iX);				
				String sX = intX.toString();
				
				//Nun wird die "Karte" gebaut 
				HexCell objCellTemp = new HexCell(new CellId("EINS", sX, sY), AreaType.OCEAN);
				
				//JPA: Die Zelle in die Datenbank packen, in der sie persistiert wird
				session.save(objCellTemp);
			}//End for iX
		}while(iY< 5);
		
		//Werte endgültig in die Datenbank übernehmen, per Hibernate
		session.getTransaction().commit();
		session.close();
	}
}


