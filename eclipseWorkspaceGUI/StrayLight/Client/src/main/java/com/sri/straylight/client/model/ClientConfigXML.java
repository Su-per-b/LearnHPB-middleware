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

// TODO: Auto-generated Javadoc
/**
 * The Class ClientConfigXML.
 */
public class ClientConfigXML {
	
	/** The Constant configFile_. */
	private static final String configFile_ = "client-config.xml";
	
	/** The Constant VERSION. */
	public static final String VERSION = "0.3";

	
	/** The Constant unzipFolder. */
	public static final String unzipFolder = "C:\\Temp\\LearnGB_0v2_VAVReheat_ClosedLoop";
	
	/** The Constant testFmuFile. */
	public static final String testFmuFile = "E:\\SRI\\modelica-projects\\fmus\\no_licence_needed\\LearnGB_VAVReheat_ClosedLoop.fmu";
	
	/** The Constant nativeLibFolder. */
	public static final String nativeLibFolder = "E:\\SRI\\straylight_repo\\visualStudioWorkspace\\bin\\Debug";
	

	/** The fmu file path. */
	public String fmuFilePath = "";
	
	/** The window icon file name. */
	public String windowIconFileName;

	
	/** The auto connect string. */
	public String autoConnectString;
	
	/** The auto parse xml string. */
	public String autoParseXMLString;
	
	/** The auto init string. */
	public String autoInitString;
	
	
	/**
	 * Load.
	 *
	 * @return the client config
	 */
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
	
	
	
	/**
	 * Convert relative to absolute.
	 *
	 * @param relativePath the relative path
	 * @return the string
	 */
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
	
	
	
	/**
	 * Make.
	 */
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
