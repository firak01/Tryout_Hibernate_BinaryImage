package tryout.hibernate;

import java.io.File;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.Session;

public class TryoutHexCellExecuteHqlMain {

	/** Hierdurch wird deutlich, dass eine STRING-WERT Spalte 
	 *  durchaus anders ist als eine INTEGER-WERT Spalte
	 * @param args
	 */
	public static void main(String[] args) {
		TryoutHexCellExecuteHqlMain tryout = new TryoutHexCellExecuteHqlMain();
		tryout.start();
	}
	
	public TryoutHexCellExecuteHqlMain() {
	}
	
	public void start() {
		/**
		 * Merke: Code um ein Image als Blob in die Datenbank zu schreiben.
		 * 1. Siehe UIHelperDummy, um mit dem InputStream zu arbeiten
		 * ...
		 * Buch.. Seite 130ff.
		 * ABER: FÜR SQLITE FUNKTIONIERT DAS SO NICHT!
		 */
		try {	
		HibernateContextProvider objContextHibernate = new HibernateContextProvider();
		
		
		//Erzeuge den Entity Manager als Ausgangspunkt für die Abfragen. !!! Damit Hibernate mit JPA funktioniert, braucht man die Datei META-INF\persistence.xml. Darin wird die persistence-unit angegeben.		
		EntityManager em = objContextHibernate.getEntityManager("TryoutHibernateBinaryImage001");
		
		Application appl = new Application();
		String sBaseDirectory = appl.getBaseDirectoryStringForDownload();
		File objDir = new File(sBaseDirectory);
		
		/*++++++++++++++*/
		//Erzeugen der Entities		
		Session session = objContextHibernate.getSession();
		
		//Vorbereiten der Wertübergabe an die Datenbank
		session.beginTransaction();
		//++++++++++++++++++
		
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
		
		
		//++++ Merke Code um ein Blob aus der Datenbank zu lesen
				/*
				 * while (rs.next()) 
		            {
		                Blob image_blob=rs.getBlob("image_100x100");
		                int blobLength = (int) image_blob.length(); 
		                byte[] blobAsBytes = image_blob.getBytes(1, blobLength); 
		                InputStream in=new ByteArrayInputStream( blobAsBytes );
		                BufferedImage image_bf = ImageIO.read(in); 
		                ImageIO.write(image_bf, "PNG", new File(folder_path+"/"+rs.getString("name"))); 
		            }
				 */
		
		//++++ Aber da das Speichern als BLOB nicht funktioniert hat, ByteArray....
		//1. HQL um eine bestimmte Zelle auszuwählen.
		
		
		//2. Aus der so gefundenen Zelle byte[] auslesen und in eine Datei zurück...
		
		

//		} catch (IOException e) {		
//			e.printStackTrace();
		} catch (Exception e) {
			 e.printStackTrace();
		}
	}
}


