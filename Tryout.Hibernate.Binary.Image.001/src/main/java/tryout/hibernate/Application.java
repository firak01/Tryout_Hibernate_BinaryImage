package tryout.hibernate;

import java.io.File;

import javax.persistence.EntityManager;

import org.hibernate.Session;

public class Application {
	
	private String sBaseDirectoryImages = null;
	private String sBaseDirectoryDownload = null;

	public Application() {		
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
	
	public String getBaseDirectoryStringForDownload(){
		if(this.sBaseDirectoryDownload==null){
			
			//Eclipse Workspace 
//			File f = new File("");
//		    String sPathEclipse = f.getAbsolutePath();		  
//		    System.out.println("Eclipse absolut path: " + sPathEclipse);
//		    
//	       String sBaseDirectory = sPathEclipse + File.separator + "images";
	       this.setBaseDirectoryStringForDownload("c:\\temp");		       
		}
		return this.sBaseDirectoryDownload;
	}
	public void setBaseDirectoryStringForDownload(String sBaseDirectory){
		this.sBaseDirectoryDownload = sBaseDirectory;
	}
}


