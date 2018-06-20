package tryout.hibernate;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.swing.JOptionPane;

import org.hibernate.Hibernate;
import org.hibernate.LobHelper;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.jdbc.NonContextualLobCreator;
import org.hibernate.engine.jdbc.StreamUtils;

/**
 * Idee zur Speicherung von Images als binäre Daten in einer Datenbank .
 * Per Hibernate und ohne "Lazy" Loading zu gefährden.
 * Siehe Buch "Persistence with Hibernate", 2nd (2016), Seiten 130ff
 * 
 * @author Fritz Lindhauer
 *
 */
public class TryoutHexCellGenerateMain {
	
	private String sBaseDirectoryImages = null;

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
		
		TryoutHexCellGenerateMain tryout = new TryoutHexCellGenerateMain();
		tryout.start();
		
		
	}
	
	public TryoutHexCellGenerateMain() {		
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

//		java.util.Properties properties = new Properties();
//		properties.load(new FileInputStream("hibernate.properties"));

		Configuration configuration = new Configuration();
//		configuration.addProperties(properties);
		configuration.configure("hibernate.cfg.xml");
	
		//Versuch, aber diese Property ggfs. auf Systemebene einstellen.
		Properties props = configuration.getProperties();
		Object prop = props.get("hibernate.jdbc.use_streams_for_binary");
		System.out.println("hibernate.jdbc.use_streams_for_binary" + "/" + prop);
		
		
		//NEIN, besser dynamisch ... String[] saFile = {"Books.png", ...
		Application appl = new Application();
		String sBaseDirectory = appl.getBaseDirectoryStringForImages();
		File objDir = new File(sBaseDirectory);

        //  Alle Dateien im "test" auflisten...
        String[] saFile = objDir.list();
        System.out.println("Folgende Bilder werden hochgeladen:");
        for (int i = 0; i < saFile.length; i++) {
        	System.out.println(saFile[i]);
        }
		
//		BufferedImage objBufferedImageTemp = null;
		//FileInputStream fis = null;
//		
//			objBufferedImageTemp = ImageIO.read(objFile);
//			
			//fis = new FileInputStream(objFile);
//			
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}	
		
//		FileInputStream fis=null;
//		byte[] imageData = new byte[(int) objFile.length()];		 
//	    fis = new FileInputStream(objFile);
//	    fis.read(imageData);
//	    fis.close();
//		
//		int byteLength = (int) objFile.length();//objBufferedImageTemp.
//		
		
		/*++++++++++++++*/
		//Erzeugen der Entities		
		Session session = objContextHibernate.getSession();
		
		//Vorbereiten der Wertübergabe an die Datenbank
		session.beginTransaction();
		//++++++++++++++++++

		//Zähler für die Bilder
		int iImageIndex = -1;
		
		//Zwei verschachtelte Schleifen, Aussen: Solange wie es "Provinzen" gibt...
		//Innen:   von 1 bis maximaleSpaltenanzahl...		
		int iY = 0;
		do{//die maximale Zeilenzahl 
			iY++;
			Integer intY = new Integer(iY);
			String sY = intY.toString();
			
			for(int iX=1; iX <= 10; iX++){	
				iImageIndex++;
				if(iImageIndex>=saFile.length) iImageIndex=0; //wieder von vorne anfangen.
				
				//++++++++++++++
				// Hole erst einmal das Bild				
				String sTileIconName = saFile[iImageIndex];
				String sFilename = sBaseDirectory + File.separator + sTileIconName;
				File objFile = new File(sFilename);
				
				//+++++++++++++++
				//Wir schreiben die Bytes weg
				byte[] imageBytes = this.getByteArrayFromFile(sFilename);				
				//+++++++++++++++
				
				
				//+++++++++++++++++++++++++++
				//Nun wird die "Karte" gebaut 
				Integer intX = new Integer(iX);				
				String sX = intX.toString();
				HexCell objCellTemp = new HexCell(new CellId("EINS", sX, sY), AreaType.OCEAN);
				
				
				//JETZT NEUE: DAS BILD HINZUFÜGEN. Siehe Buch "Persistence with Hibernate" , 2nd (2016), S. 131
				//Beispieldate: LazyProperties.java
				//ABER: Das scheint nur mit JDBC Treibern zu funktionieren die JDBC 4 unterstützen...
				LobHelper objHelperLob = session.getLobHelper();
				Blob imageBlob = objHelperLob.createBlob(imageBytes); 	//.createBlob(fis, byteLength);
					
				//Versuch, den NonContextualLobCreator zu verwenden...
//				Blob imageBlob = NonContextualLobCreator.INSTANCE.createBlob(imageBytes);				
				
//				Fehler:
//					java.lang.UnsupportedOperationException: Blob may not be manipulated from creating session
//					at org.hibernate.engine.jdbc.BlobProxy.invoke(BlobProxy.java:166)
//					at com.sun.proxy.$Proxy15.setBinaryStream(Unknown Source)
//				Blob imageBlob = Hibernate.getLobCreator(session).createBlob(new byte[0]);
//				OutputStream setBinaryStream = imageBlob.setBinaryStream(1);
//				StreamUtils.copy(fis, setBinaryStream);//das kenn ich aber nicht: Utils.fastChannelCopy(input, setBinaryStream);
//				setBinaryStream.close();
				//objCellTemp.setImage01(imageBlob);
				
				//Fehler auch mit neuem JDBC Treiber und neuer SQLITE Datenbank: 
//				Caused by: java.sql.SQLFeatureNotSupportedException
//				at org.sqlite.jdbc4.JDBC4PreparedStatement.setBinaryStream(JDBC4PreparedStatement.java:78)
//				at org.hibernate.type.descriptor.sql.BlobTypeDescriptor$5$1.doBind(BlobTypeDescriptor.java:133)
				
				//FEHLER: 
//				int byteLength = (int) objFile.length();//objBufferedImageTemp.
//				byte[] bytes = new byte[byteLength];//new byte[131072];
//				InputStream imageInputStream = new ByteArrayInputStream(bytes);
//								
//				Blob imageBlob = session.getLobHelper().createBlob(fis, byteLength);
//				objCellTemp.setImage01(imageBlob);
					
									
				/*Letzte Alternative... Aber das brauchen wir nicht....
				 * You can always save the record using plain Hibernate but leave the blob column NULL and then set the blob column afterwards using low level JDBC. As a workaround should nothing else present itself (which I personally consider to be an actual solution to fit the design, not a workaround).				 
				 */
				
				//Statt Blob als Datentyp in der Modelklasse zu verwenden, hier byte[] verwenden, also versuchen direct die Bytes zu schreiben.
				//Das bekommt man auch persistiert.
				//Aber: Streamw wären besser. Diese Vorgehensweise soll wertvollen Java-Heap-Speicher verbrauchen. Alles wird dort erst einmal abgelegt.
				//objCellTemp.setImage01(imageBytes);
				objCellTemp.setImage01Blob(imageBlob);
				objCellTemp.setImage01Name(sTileIconName);
				
				//Das wäre das Auslesen eines schon gespeicherten Bilds
//				InputStream imageDataStream = objCellTemp.getImage01().getBinaryStream();				
//				ByteArrayOutputStream outStream = new ByteArrayOutputStream();
//				StreamUtils.copy(imageDataStream, outStream);
//				StreamUtils.copy(fis, outStream);
//				
//				byte[] imageBytes = outStream.toByteArray();
//				int byteLength = imageBytes.length;
					
//					BufferedImage bIResult = null;
//					// Fill your bufferedImage
//					ByteArrayOutputStream bos = new ByteArrayOutputStream();
//					boolean resultWrite = ImageIO.write(bIResult, "PNG", bos);
//					byte[] imageInBytes = bos.toByteArray();
//					int length = imageInBytes.length;
				
				
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} 
//				catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				
				//JPA: Die Zelle in die Datenbank packen
				session.save(objCellTemp);
			}//End for iX
		}while(iY< 5);

		

		
		//Werte endgültig in die Datenbank übernehmen, per Hibernate
		session.getTransaction().commit();
		session.close();
		JOptionPane.showMessageDialog(null, "Images saved successfully!","Successfull",JOptionPane.INFORMATION_MESSAGE); 
		

//		} catch (IOException e) {		
//			e.printStackTrace();
		} catch (Exception e) {
			 e.printStackTrace();
		}
	}
	
	//#### METHODEN FÜR PFADE
	public String getBaseDirectoryStringForImages(){
		if(this.sBaseDirectoryImages==null){
			
			//Eclipse Workspace 
			File f = new File("");
		    String sPathEclipse = f.getAbsolutePath();		  
		    System.out.println("Eclipse absolut path: " + sPathEclipse);
		    
	       String sBaseDirectory = sPathEclipse + File.separator + "images";
	       this.setBaseDirectoryStringForImages(sBaseDirectory);		       
		}
		return this.sBaseDirectoryImages;
	}
	public void setBaseDirectoryStringForImages(String sBaseDirectory){
		this.sBaseDirectoryImages = sBaseDirectory;
	}
	
	
	
	
    /**private method to get byte array from bytestream 
     * * 
     *  
     */  
    private byte[] getByteArrayFromFile(String filePath){  
        byte[] result=null;  
        FileInputStream fileInStr=null;  
        try{  
            File imgFile=new File(filePath);  
            fileInStr=new FileInputStream(imgFile);  
            long imageSize=imgFile.length();  
              
            if(imageSize>Integer.MAX_VALUE){  
                return null;    //image is too large  
            }  
              
            if(imageSize>0){  
                result=new byte[(int)imageSize];  
                fileInStr.read(result);  
            }  
        }catch(Exception e){  
            e.printStackTrace();  
        } finally {  
            try {  
                fileInStr.close();  
            } catch (Exception e) {  
            }  
        }  
        return result;  
    }  
    
}


