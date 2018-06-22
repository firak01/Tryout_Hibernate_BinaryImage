package tryout.hibernate;

import java.io.Serializable;
import java.sql.Blob;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

/**Klasse für eine HexEck Zelle - persistierbar per JPA. Wird von AreaCell geerbt. 
 * Die Klasse HexCellTHM hat darüber hinaus noch weitere Aufgaben deiner Swing - Komponente.
 * Wegen nicht zu persistierender Eigenschaften wurde dann diese speziell nur mit zu persistierenden Eigenschaften erstellt.
 * @author lindhaueradmin
 *
 */

//Vgl. Buch "Java Persistence API 2", Seite 34ff. für @Table, @UniqueConstraint
@Entity
@Access(AccessType.FIELD)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="Disc", discriminatorType = DiscriminatorType.STRING)
@Table(name="HEXCELL")
public class HexCell implements Serializable{
	private static final long serialVersionUID = 1113434456411176970L;
	
	//Realisierung eines Zusammengesetzten Schlüssels
	//Siehe Buch "Java Persistence API 2", Seite 48ff.
	@EmbeddedId
	
	//Merke: Attribut Access über FIELD.
	@AttributeOverrides({
			@AttributeOverride(name = "sMapAlias", column= @Column(name="MAPALIAS")),
			@AttributeOverride(name = "sMapX", column= @Column(name="X", length = 2)),
			@AttributeOverride(name = "sMapY", column= @Column(name="Y", length = 2))
	})
	//Merke: Attribut Access über PROPERTY.
//	@AttributeOverrides({
//		@AttributeOverride(name = "mapAlias", column= @Column(name="MAPALIAS")),
//		@AttributeOverride(name = "mapX", column= @Column(name="X", length = 2)),
//		@AttributeOverride(name = "mapY", column= @Column(name="Y", length = 2))
//	})
	private CellId id;
	
	
	//NEU IN DIESEM TRYOUT: BLOB	
	//private java.sql.Blob imageBlob01; //Das geht so nicht. Liegt vermutlich am JDBC Treiber für die SQLITE Datenbank
	@Transient
	private Blob blobImageBlob01;
	
	//Alternativer Lösungsversuch, aber nur als Byte-Array
	@Transient
	private byte[] imageBlob01;
	
	
	
	@Transient
	private String sImageBlob01;
	
	@Transient
	private Long lngImageBlob01;
	
	
	//Der Default Contruktor wird für JPA - Abfragen wohl benötigt
	 public HexCell(){
	 }
	 public HexCell(CellId objId, Enum<AreaType> enumAreaType){
		 this.id = objId;
	 }
	 
	 //Siehe Buch "Java Persistence API", Seite 37ff
	 @Transient
	 public String getFieldAlias(){
		return this.getMapAlias() + "#" + this.getMapX() + "-" + this.getMapY(); 
	 }
	 
	 
	 //### getter / setter
		public CellId getId(){
			return this.id;
		}
		
		@Access(AccessType.PROPERTY)
		@Column(name="MAPMAP", nullable=false, columnDefinition="integer default 0")
		public String getMapAlias(){
		   	return this.getId().getMapAlias();
		}
		public void setMapAlias(String sAlias){						
			this.getId().setMapAlias(sAlias);
		}
		
		
		//Versuch mit MAX(X) darauf zuzugreifen aus der Methode fillMap(..)
		//ABER: Da das String ist, wird "9" als maximaler Wert zurückgeliefert und kein Integerwert.	
		@Access(AccessType.PROPERTY)
		@Column(name="XX", nullable=false, columnDefinition="integer default 0")
	    public int getMapX(){
	    	String stemp = this.getId().getMapX();
	    	Integer objReturn = new Integer(stemp);
	    	return objReturn.intValue();
	    	//return objReturn;
	    }
		public void setMapX(int iValue){
			Integer intValue = new Integer(iValue);
			String sX = intValue.toString();
			this.getId().setMapX(sX);
		}
	    
		@Access(AccessType.PROPERTY)
		@Column(name="YY", nullable=false, columnDefinition="integer default 0")
	    public int getMapY(){
			String stemp =  this.getId().getMapY();
	    	Integer objReturn = new Integer(stemp);
	    	return objReturn.intValue();
	    	//return objReturn;
	    }
		public void setMapY(int iValue){
			Integer intValue = new Integer(iValue);
			String sY = intValue.toString();
			this.getId().setMapY(sY);
		}
		
		@Access(AccessType.PROPERTY)
		//ABER SQLite: Probleme beim Holen der Daten per HQL. ... @Lob auch ohne dies Annotation wird ein blob in der Datenbank angelegt... UND Nur so kann dann per HQL wieder auf diese Zelle zugegriffen werden. 
		//Merke: dependent on the hibernate version, the Lob annotation could have no type parameter. quote from here: @Lob no longer has attributes, the lob type (CLOB, BLOB) is guessed. If the underlying type is a String or an array of character then CLOB are used. Othersise BLOB are used.		
		@Column(name="image01", nullable=true)	
		public  byte[] getImage01() {
			return this.imageBlob01;
		}		
		public void setImage01(byte[] imageBlob) {
			this.imageBlob01 = imageBlob;
		}
		
		@Access(AccessType.PROPERTY)
		@Column(name="image01name", nullable=false)
		public String getImage01Name() {
			return this.sImageBlob01;
		}
		public void setImage01Name(String sFileName) {
			this.sImageBlob01 = sFileName;
		}
		
		@Access(AccessType.PROPERTY)
		@Column(name="image01length", nullable=false)
		public long getImage01Length() {
			return this.lngImageBlob01;
		}
		public void setImage01Length(long lngFileSize) {
			this.lngImageBlob01 = lngFileSize;
		}
		
//		@Access(AccessType.PROPERTY)
//		@Lob //Merke: dependent on the hibernate version, the Lob annotation could have no type parameter. quote from here: @Lob no longer has attributes, the lob type (CLOB, BLOB) is guessed. If the underlying type is a String or an array of character then CLOB are used. Othersise BLOB are used.
//		@Column(name="image01blob", nullable=true)	
//	    public Blob getImage01Blob() {
//			return this.blobImageBlob01;
//		}		
//		public void setImage01Blob(Blob imageBlob) {
//			this.blobImageBlob01 = imageBlob;
//		}
//	    
}
