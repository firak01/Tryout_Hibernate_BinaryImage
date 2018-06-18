package tryout.hibernate;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import tryout.hibernate.HexCell;

public class HibernateContextProvider{
	
	/**Hier wird die Konfiguration programmatisch aufgebaut
	 * und die globalen Hibernate Objekte sind hierüber überall verfügbar.
	 * 
	 */
	private Configuration cfgHibernate = new Configuration();
	private Session objSession = null;
	
	
	public HibernateContextProvider() {
		boolean bErg = this.fillConfiguration();
		if(!bErg){
		}
	}
	
	/**Fülle die Configuration
	 * a) mit globalen Werten, z.B. Datenbankname, Dialekt
	 * b) mit den zu betrachtenden Klassen, entweder annotiert oder per eigener XML Datei.
	 * 
	 * @return
	 * @throws ExceptionZZZ 
	 */
	public boolean fillConfiguration() {
		boolean bReturn = false;
		
		bReturn = fillConfigurationGlobal();
		//+++ Die für Hiberante konfigurierten Klassen hinzufügen
		//Merke: Wird eine Klasse ohne @Entity hinzugefügt, gibt es folgende Fehlermeldung: Exception in thread "main" org.hibernate.AnnotationException: No identifier specified for entity: use.thm.client.component.AreaCellTHM
		bReturn = addConfigurationAnnotatedClass(HexCell.class);
		//bReturn = addConfigurationAnnotatedClass(AreaCell.class);
		
		return bReturn;
	}
	
	/** Fülle globale Werte in das Configuruation Objekt, z.B. der Datenbankname, Dialekt, etc.
	 * 
	 */
	public boolean fillConfigurationGlobal(){
				//TODO: Die hier verwendeten Werte aus der Kernel-Konfiguration auslesen.
				//Programmatisch das erstellen, das in der hibernate.cfg.xml Datei beschrieben steht.
				//Merke: Irgendwie funktioniert es nicht die Werte in der hibernate.cfg.xml Datei zu überschreiben.
		 		//			Darum muss z.B. hibernate.hbm2ddl.auto in der Konfigurationdatei auskommentiert werden, sonst ziehen hier die Änderungen nicht.
				this.getConfiguration().setProperty("hiberate.show_sql", "true");
				this.getConfiguration().setProperty("hiberate.format_sql", "true");
				this.getConfiguration().setProperty("hibernate.dialect","tryout.hibernate.SQLiteDialect" );
				this.getConfiguration().setProperty("hibernate.connection.driver_class", "org.sqlite.JDBC");
				this.getConfiguration().setProperty("hibernate.connection.url", "jdbc:sqlite:c:\\server\\SQLite\\TryoutHibernateBinaryImage001.sqlite");
				this.getConfiguration().setProperty("hibernate.connection.username", "");
				this.getConfiguration().setProperty("hibernate.connection.password", "");

				/*
				 * So the list of possible options are,
    validate: validate the schema, makes no changes to the database.
    update: update the schema.
    create: creates the schema, destroying previous data.
    create-drop: drop the schema when the SessionFactory is closed explicitly, typically when the application is stopped.
				 */
				this.getConfiguration().setProperty("hibernate.hbm2ddl.auto", "create"); //! Damit wird die Datenbank und sogar die Tabellen darin automatisch erstellt, aber: Sie wird am Anwendungsende geleert.
				//this.getConfiguration().setProperty("hibernate.hbm2ddl.auto", "update");  //! Jetzt erst wird jede Tabelle über den Anwendungsstart hinaus gepseichert.
				this.getConfiguration().setProperty("cache.provider_class", "org.hiberniate.cache.NoCacheProvider");
				this.getConfiguration().setProperty("current_session_context_class", "thread");
				
				return true;
	}
	
	/** Wird eine zu persisierende Klasse nicht der Konfiguration übergeben, kommt es z.B. zu folgender Fehlermeldung
	 *  Exception in thread "main" org.hibernate.MappingException: Unknown entity: use.thm.client.component.AreaCellTHM
	 * @param cls
	 * @return
	 * @throws ExceptionZZZ
	 */
	public boolean addConfigurationAnnotatedClass(Class cls){		
		this.getConfiguration().addAnnotatedClass(cls);
		return true;
	}
	/**Für die so hinzugefügte Klasse muss es eine XML Konfigurationsdatei geben.
	 * Ansonsten Fehler, z.B. für eine User.class : org.hibernate.MappingNotFoundException: resource: tryout/hibernate/User.hbm.xml not found
	 * 
	 * Wird eine zu persisierende Klasse nicht der Konfiguration übergeben, kommt es z.B. zu folgender Fehlermeldung
	 * Exception in thread "main" org.hibernate.MappingException: Unknown entity: use.thm.client.component.AreaCellTHM
	 * @param cls
	 * @return
	 * @throws ExceptionZZZ
	 */
	public boolean addConfigurationClass(Class cls){		
		this.getConfiguration().addClass(cls);
		return true;
	}
	
	//####### GETTER / SETTER
	public Configuration getConfiguration(){
		return this.cfgHibernate;
	}
	
	public Session getSession() {
		Session objReturn = this.objSession;
		if(objReturn==null || objReturn.isOpen()==false){
			Configuration cfg = this.getConfiguration();
			
			ServiceRegistry sr = new ServiceRegistryBuilder().applySettings(cfg.getProperties()).buildServiceRegistry();
			//deprecated: SessionFactory sf = cfg.buildSessionFactory();
			SessionFactory sf = cfg.buildSessionFactory(sr);
			objReturn = sf.openSession();
			this.objSession = objReturn;
		}
		return objReturn;
	}

	@SuppressWarnings("unchecked")
	public EntityManager getEntityManager(String sSchemaName){
		EntityManager objReturn = null;
		
		main:{
			try{			
				//Neu erstellen
				EntityManagerFactory emf = Persistence.createEntityManagerFactory(sSchemaName);
				objReturn = emf.createEntityManager();
			}catch(PersistenceException pe){
				pe.printStackTrace();				
			}
		}//end main:
		return objReturn;
	}
}

