package com.sri.straylight.client.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

import com.sri.straylight.client.ConnectTo;
import com.sri.straylight.fmuWrapper.model.FMUwrapperConfig;
import com.thoughtworks.xstream.XStream;

public class ClientConfigXML {
	
	private static final String configFile_ = "client-config.xml";
	
	public static final String VERSION = "0.3";

	
	public static final String unzipFolder = "C:\\Temp\\LearnGB_0v2_VAVReheat_ClosedLoop";
	public static final String testFmuFile = "E:\\SRI\\modelica-projects\\fmus\\no_licence_needed\\LearnGB_VAVReheat_ClosedLoop.fmu";
	public static final String nativeLibFolder = "E:\\SRI\\straylight_repo\\visualStudioWorkspace\\bin\\Debug";
	

	public String fmuFilePath = "";
	public String windowIconFileName;

	
	public String autoConnectString;
	public String autoParseXMLString;
	public String autoInitString;
	
	
	public static ClientConfig load() {
		
		String theOs = System.getProperty("os.name");
		System.out.println( "ClientConfig detected operating system : " + theOs);
		
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		
		URL configFileUrl = classLoader.getResource(configFile_);

		XStream xStream = new XStream();
		xStream.alias("config", com.sri.straylight.client.model.ClientConfigXML.class);
		ClientConfigXML configXML;
		
		try {
			
			String filePath = configFileUrl.getFile();
			FileInputStream fi = new FileInputStream (filePath);
			
			Object object = xStream.fromXML(fi);
					
			configXML = (ClientConfigXML)object;
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
		
		ClientConfig clientConfig = new ClientConfig();
		clientConfig.connectTo = ConnectTo.connecTo_file;

		clientConfig.windowIconUrl = classLoader.getResource(configXML.windowIconFileName);
		clientConfig.autoConnectFlag = Boolean.valueOf(configXML.autoConnectString);
		clientConfig.autoParseXMLFlag = Boolean.valueOf(configXML.autoParseXMLString);
		clientConfig.autoInitFlag = Boolean.valueOf(configXML.autoInitString);
		
		
		
		return clientConfig;
	}
	
	
	
	private static String convertRelativeToAbsolute (String relativePath) {

		
		String absolutePath ="";
		
		try {

			File file = new File (".");
			String currentDirectory = file.getCanonicalPath();
			
			String complexFolderBase = currentDirectory + relativePath;
			
			File file2 = new File (complexFolderBase + "\\.");
			String simpleFolderBase = file2.getCanonicalPath();
			
			absolutePath = simpleFolderBase;
			//absolutePath = simpleFolderBase + "\\" + config.unzippedFMU;

			   
			 } catch(Exception e) {
				 System.out.println("Exceptione is ="+e.getMessage());
				 return null;
			 }
		
		return absolutePath;
	
	}
	
	
	
	public static void make() {
		ClientConfigXML config = new ClientConfigXML();
		
		XStream xStream = new XStream();
		xStream.alias("config", com.sri.straylight.fmuWrapper.model.FMUwrapperConfig.class);
		
		//serialize
		String xmlStr = xStream.toXML(config);
		URL configFileUrl = ClientConfigXML.class.getClassLoader().getResource(configFile_);
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
