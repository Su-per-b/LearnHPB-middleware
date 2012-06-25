package com.sri.straylight.fmuWrapper.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

import com.thoughtworks.xstream.XStream;

public class FMUwrapperConfig {
	
	private static final String configFile_ = "config.xml";
	
	//public double timeDelta;
	//public double timeEnd;
	public String unzipFolderBase;
	public String unzippedFMU;
	public String unzipFolder;
	public String nativeLibFolder;


	
	
	//public String unzipFolder = "C:\\Temp\\LearnGB_0v2_VAVReheat_ClosedLoop";
	//public String testFmuFile = "E:\\SRI\\modelica-projects\\fmus\\no_licence_needed\\LearnGB_VAVReheat_ClosedLoop.fmu";
//	public String nativeLibFolder = "E:\\SRI\\straylight_repo\\visualStudioWorkspace\\bin\\Debug";
	public static FMUwrapperConfig load() {
		
		String theOs = System.getProperty("os.name");
		System.out.println( "Operating system detected: " + theOs);
		
		
		URL configFileUrl = FMUwrapperConfig.class.getClassLoader().getResource(configFile_);

		XStream xStream = new XStream();
		xStream.alias("config", com.sri.straylight.fmuWrapper.model.FMUwrapperConfig.class);
		FMUwrapperConfig config;
		
		try {
			FileInputStream fi = new FileInputStream (configFileUrl.getFile());
			config = (FMUwrapperConfig)xStream.fromXML(fi);
			fi.close();
			
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		

		
		try {
			File file = new File (".");
			String currentDirectory = file.getCanonicalPath();
			
			String complexUnzipFolderBase = currentDirectory + config.unzipFolderBase;// + config.unzippedFMU;
			
			File file2 = new File (complexUnzipFolderBase + "\\.");
			String simpleUnzipFolderBase = file2.getCanonicalPath();
			
			config.unzipFolderBase = simpleUnzipFolderBase;
			config.unzipFolder = simpleUnzipFolderBase + "\\" + config.unzippedFMU;
			

			   
			   
			 }catch(Exception e) {
				 System.out.println("Exceptione is ="+e.getMessage());
				 return null;
			 }
		
		

		return config;
		
	}
	
	public static void make() {
		FMUwrapperConfig config = new FMUwrapperConfig();
		
		XStream xStream = new XStream();
		xStream.alias("config", com.sri.straylight.fmuWrapper.model.FMUwrapperConfig.class);
		
		//serialize
		String xmlStr = xStream.toXML(config);
		URL configFileUrl = FMUwrapperConfig.class.getClassLoader().getResource(configFile_);
		String filePath = configFileUrl.getFile();
		
       	int idx = filePath.lastIndexOf(".");
       	String newfilePath = filePath.substring(0, idx) ;
       	newfilePath += "_out.xml";
       	
		
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(newfilePath));
			out.write(xmlStr);
			out.close();
		} 
		catch (IOException e) { 
			e.printStackTrace();
		}
		
		
	}
	
	
}
