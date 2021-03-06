package com.sri.straylight.fmuWrapper.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import com.thoughtworks.xstream.XStream;

// TODO: Auto-generated Javadoc
/**
 * The Class FMUwrapperConfig.
 */
public class FMUwrapperConfig {
	
	/** The Constant configFile_. */
	private static String configFile_ = "fmuwrapper-config.xml";
	private static FMUwrapperConfig config;
	
	//in XML file
	public String id;
	public String nativeLibFolderRelativePath;
	public String unzipFolderRelativePath;
	public String fmuFolderName;
	public String parseInternalVariablesFlagStr;
	public String parseInternalVariableLimitStr;
	public String scalarVariablePrecisionStr;
	public String msPerTimeStepStr;
	
	//derived
	public String unzipFolderAbsolutePath;
	public String fmuFolderAbsolutePath;
	public String nativeLibFolderAbsolutePath;
	public Boolean parseInternalVariablesFlag;
	public Integer parseInternalVariableLimit;
	public Integer scalarVariablePrecision;
	public Integer msPerTimeStep;
	
	//override
	public static File fmuFolderAbsolutePathOverride = null;
	
	
	public static FMUwrapperConfig load(String configFile) {
		configFile_ = configFile;
		return FMUwrapperConfig.load();
	}
	
	public static FMUwrapperConfig load() {
		
		
		if (null == config) {
			String theOs = System.getProperty("os.name");
			
			System.out.println( "FMUwrapperConfig detected operating system : " + theOs);
			
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			InputStream is = classLoader.getResourceAsStream(configFile_);
			
			XStream xStream = new XStream();
			xStream.alias("config", com.sri.straylight.fmuWrapper.model.FMUwrapperConfig.class);
			
			try {
				
				Object object = xStream.fromXML(is);
				
				config = (FMUwrapperConfig)object;
				is.close();

			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
			
			
			
			config.unzipFolderAbsolutePath = convertRelativeToAbsolute(config.unzipFolderRelativePath);
			
			
			if (null == fmuFolderAbsolutePathOverride) {
				config.fmuFolderAbsolutePath = config.unzipFolderAbsolutePath + "\\" + config.fmuFolderName;
			} else {
				config.fmuFolderAbsolutePath = fmuFolderAbsolutePathOverride.toString();
			}

			
			
			
			config.nativeLibFolderAbsolutePath = convertRelativeToAbsolute(config.nativeLibFolderRelativePath);
			
			config.parseInternalVariablesFlag = Boolean.parseBoolean(config.parseInternalVariablesFlagStr);
			config.parseInternalVariableLimit = Integer.parseInt(config.parseInternalVariableLimitStr);
			config.scalarVariablePrecision = Integer.parseInt(config.scalarVariablePrecisionStr);
			config.msPerTimeStep = Integer.parseInt(config.msPerTimeStepStr);
			
		}

		
		return config;
		
	}
	
	/**
	 * Convert relative to absolute.
	 *
	 * @param relativePath the relative path
	 * @return the string
	 */
	public static String convertRelativeToAbsolute (String relativePath) {

		
		String absolutePath ="";
		
		try {

			File file = new File (".");
			String currentDirectory = file.getCanonicalPath();
			
			String complexFolderBase = currentDirectory + relativePath;
			
			File file2 = new File (complexFolderBase + "\\.");
			String simpleFolderBase = file2.getCanonicalPath();
			
			absolutePath = simpleFolderBase;
			//absolutePath = simpleFolderBase + "\\" + config.unzippedFMU;

			   
			 }catch(Exception e) {
				 System.out.println("Exceptione is ="+e.getMessage());
				 return null;
			 }
		
		return absolutePath;
	
	}
	
	
	
	/**
	 * Make.
	 */
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
