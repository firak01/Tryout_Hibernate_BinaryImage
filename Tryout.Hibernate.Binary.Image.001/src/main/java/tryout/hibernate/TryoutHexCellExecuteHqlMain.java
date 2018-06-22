package tryout.hibernate;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

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
				javax.persistence.Query objQuery = em.createQuery(sQueryTemp);
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
		session.clear();
		session.close();
		
		
		
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
		List<HexCell> listaHex = this.searchHexCell("EINS", "2", "1");
		System.out.println("Anzahl gefundener Felder: " + listaHex.size());
		
		//http://www.codejava.net/frameworks/hibernate/hibernate-binary-data-and-blob-mapping-example
		//2. Aus der so gefundenen Zelle byte[] auslesen und in eine Datei zurück...
		for(HexCell objCell : listaHex) {
			byte[] byteImage = objCell.getImage01();
			if(byteImage!=null) {
				BufferedImage objBufferedImage = ImageIO.read(new ByteArrayInputStream(byteImage));
				ImageIcon objImageIconReturn = new ImageIcon(objBufferedImage);
				//JOptionPane.showMessageDialog(null, "Images saved successfully!","Successfull",JOptionPane.INFORMATION_MESSAGE); 
				
				
				 JDialog dialog = new JDialog();     
                 dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                 dialog.setTitle("Image Loading Demo");

                 dialog.add(new JLabel(objImageIconReturn)); //Das wäre, wenn man es direkt aus der Datei liest: new ImageIcon(ImageIO.read(getClass().getResourceAsStream(IMAGE_URL)))));

                 dialog.pack();
                 dialog.setLocationByPlatform(true);
                 dialog.setVisible(true);
			}
		}
		


		

//		} catch (IOException e) {		
//			e.printStackTrace();
		} catch (Exception e) {
			 e.printStackTrace();
		}
	}
	
	/** Hole das HexFeld per HQL.
	 *  Merke: Hier wird eine Hibernate Query verwendet, im Gegensatz zu javax.persistence.Query
	 * @param sMapAlias
	 * @param sX
	 * @param sY
	 * @return
	 */
	public List<HexCell> searchHexCell(String sMapAlias, String sX, String sY){
		List<HexCell> listReturn = new ArrayList<HexCell>();
		
		HibernateContextProvider objContextHibernate = new HibernateContextProvider();
				
		//Erzeuge den Entity Manager als Ausgangspunkt für die Abfragen. !!! Damit Hibernate mit JPA funktioniert, braucht man die Datei META-INF\persistence.xml. Darin wird die persistence-unit angegeben.		
		EntityManager em = objContextHibernate.getEntityManager("TryoutHibernateBinaryImage001");
				
		/*++++++++++++++*/
		//Erzeugen der Entities		
		Session session = objContextHibernate.getSession();
		
		
		
//		select mate
//		from Cat as cat
//		    inner join cat.mate as mate
		    
		//1. Beispiel: wenn man aber die WHERE Parameter so als String reinprogrammiert, ist das anfällig für SQL injection.
		//String sHql = "SELECT id from Tile as tableTile";								
		//listReturn = this.findByHQL(sHql, 0, 0);//start ist indexwert also 0 = erster Wert, Danach folgt maximale Anzahl von Objekten.
		
		//2. Beispiel: Etwas sicherer ist es die Parameter mit Platzhaltern zu füllen
		//Session session = this.getSession();
		//liefert die ID Spalte als Integer zurück, also nicht das TileId Objekt...  Query query = session.createQuery("SELECT id from Tile as tableTile");
		//                                                       wird nicht gefunden Query query = session.createQuery("SELECT TileIdObject from Tile as tableTile");
		
		//Also über die HEXCELL gehen...
		//JA, das liefert die HEXCELL-Objekte zurück
		//Query query = session.createQuery("SELECT objHexCell from Tile as tableTile");
						
		//JA, das liefert die CellId-Objekte der Hexcell zurück
		//Query query = session.createQuery("SELECT objHexCell.id from Tile as tableTile");
		
		//JA, das liefert die Alias Map-Werte zurück
		//Query query = session.createQuery("SELECT objHexCell.id.mapAlias from Tile as tableTile");
		
		//DARAUS VERSUCHEN DIE ABFRAGE ZU BAUEN....
		//Query query = session.createQuery("SELECT objHexCell from Tile as tableTile where tableTile.objHexCell.Id.MapAlias IN (:mapAlias)");
		
			
		//JA, das funktioniert
		//Merke: DAs hotl im TileHexMap Projekt Spielstein-Objekte
		//Query query = session.createQuery("from Tile as tableTile where tableTile.objHexCell.id.mapAlias = :mapAlias");
		//Query query = session.createQuery("from Tile as tableTile where tableTile.objHexCell.id.mapAlias = :mapAlias AND tableTile.objHexCell.id.mapX = :mapX AND tableTile.objHexCell.id.mapY = :mapY");

		//Merke: Das holt im TileHexMap Projekt Truppen-Objekte
		//Query query = session.createQuery("from TroopArmy as tableTile where tableTile.objHexCell.id.mapAlias = :mapAlias AND tableTile.objHexCell.id.mapX = :mapX AND tableTile.objHexCell.id.mapY = :mapY");
		
		//Hole eine HexCell
		//Query query = session.createQuery("from HexCell as tableHex where tableHex.id.mapAlias = :mapAlias AND tableHex.mapX = :mapX AND tableHexmapY = :mapY");		
		Query query = session.createQuery("from HexCell as tableHex where tableHex.mapAlias = :mapAlias AND tableHex.mapX = :mapX AND tableHex.mapY = :mapY");
		
		query.setString("mapAlias", sMapAlias);
		query.setString("mapX", sX);
		query.setString("mapY", sY);
		
		//Object objResult = query.uniqueResult(); //Das sind aber ggfs. mehrere Werte		
		listReturn = query.list(); 
		
		//3. Beispiel
		//TODO: Nicht den statischen HQL Ansatz, sondern über die Criteria API, d.h. die Where - Bedingung zur Laufzeit zusammensetzen
				
		return listReturn;
	}
}


