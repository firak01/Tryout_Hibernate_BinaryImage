package tryout.hibernate;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.Session;

public class TryoOutHexCellExecuteHqlMain {

	/** Hierdurch wird deutlich, dass eine STRING-WERT Spalte 
	 *  durchaus anders ist als eine INTEGER-WERT Spalte
	 * @param args
	 */
	public static void main(String[] args) {
		HibernateContextProvider objContextHibernate = new HibernateContextProvider();
		
		
		//Erzeuge den Entity Manager als Ausgangspunkt für die Abfragen. !!! Damit Hibernate mit JPA funktioniert, braucht man die Datei META-INF\persistence.xml. Darin wird die persistence-unit angegeben.		
		EntityManager em = objContextHibernate.getEntityManager("TryOutHibernateBinaryImage001");
		
		//TODO: Prüfe die Existenz der Datenbank ab. Ohne die erstellte Datenbank und die Erstellte Datenbanktabelle kommt es hier zu einem Fehler.
		//           Darum muss ich den Code immer erst auskommentieren, nachdem ich die Datenbank gelöscht habe.
		
		
		/* FRAGE HIER EINE SPALTE AB; DIE NUR STRING WERTE BEINHALTET */
		System.out.println("######## STRING SPALTE ABFRAGE AUS ID.... BRINGT GANZ KOMISCHES ERGEBNIS #######");
		//TODO: Mache ein DAO Objekt und dort diesen HQL String hinterlegen.
				//TODO: Anzahl der echten Elemente aus einer noch zu erstellenden Hibernate-ZKernelUtility-Methode holen, sowie eine ResultList OHNE NULL Objekte.
				//ACCES TYPE PROPERTY String sQueryTemp = "SELECT MAX(c.id.mapX) FROM HexCell c";
				//ACCESS TYPE FIELD
				String sQueryTemp = "SELECT MAX(c.id.sMapX) FROM HexCell c";
				Query objQuery = em.createQuery(sQueryTemp);
				Object objSingle =objQuery.getSingleResult();
				if(objSingle!=null){
					System.out.println("Gefundenes Objekt obj.class= " + objSingle.getClass().getName());
					System.out.println("Objekt als Single Result der Query: HASHCODE " + objSingle.hashCode());// Gibt was ganz seltsames aus '57', halt ein HASHCODE
					System.out.println("Objekt als Single Result der Query: toSTRING " + objSingle.toString());//Gibt '9' aus. DAs ist als String größer als '10'!!!
					
				}else{
					System.out.println("NULL Objekt als Single Result der Query " + sQueryTemp);
				}
				
				List objResult = objQuery.getResultList();
				
				//TODO: WENN Die Anzahl der Zellen in der Datenbank leer ist, dann diese neu aufbauen/füllen
				for(Object obj : objResult){
					if(obj==null){										
						System.out.println("NULL Objekt in ResultList der Query " + sQueryTemp);
					}else{					
						System.out.println("Gefundenes Objekt obj.class= " + obj.getClass().getName());
					}
				}
				
		/* FRAGE HIER DIE SPALTE AB, DIE ECHTE INTEGER WERTE BEINHALTET */
		System.out.println("######## INTEGER SPALTE ABFRAGE AUS ID #######");
				
		//TODO: Mache ein DAO Objekt und dort diesen HQL String hinterlegen.
		//TODO: Anzahl der echten Elemente aus einer noch zu erstellenden Hibernate-ZKernelUtility-Methode holen, sowie eine ResultList OHNE NULL Objekte.
		sQueryTemp = "SELECT MAX(c.mapX) FROM HexCell c";
		objQuery = em.createQuery(sQueryTemp);
		objSingle =objQuery.getSingleResult();
		if(objSingle!=null){
			System.out.println("Gefundenes Objekt obj.class= " + objSingle.getClass().getName());
			System.out.println("Objekt als Single Result der Query: HASHCODE " + objSingle.hashCode());
			System.out.println("Objekt als Single Result der Query: toSTRING " + objSingle.toString());
		}else{
			System.out.println("NULL Objekt als Single Result der Query " + sQueryTemp);
		}
		
		objResult = objQuery.getResultList();
		
		//TODO: WENN Die Anzahl der Zellen in der Datenbank leer ist, dann diese neu aufbauen/füllen
		for(Object obj : objResult){
			if(obj==null){										
				System.out.println("NULL Objekt in ResultList der Query " + sQueryTemp);
			}else{					
				System.out.println("Gefundenes Objekt obj.class= " + obj.getClass().getName());
			}
		}
		
		

	}
}


