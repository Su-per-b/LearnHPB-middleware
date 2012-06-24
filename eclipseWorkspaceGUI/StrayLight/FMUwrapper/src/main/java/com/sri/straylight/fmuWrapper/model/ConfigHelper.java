package com.sri.straylight.fmuWrapper.model;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

import com.thoughtworks.xstream.XStream;



public class ConfigHelper {

	private static final String configFile = "config.xml";
	

	
	public static FMUwrapperConfig load() {
		
		String theOs = System.getProperty("os.name");
		System.out.println( "Operating system detected: " + theOs);
		
		
		URL configFileUrl = ConfigHelper.class.getClassLoader().getResource(configFile);

		XStream xStream = new XStream();
		xStream.alias("config", com.sri.straylight.fmuWrapper.model.FMUwrapperConfig.class);
		
		try {
			FileInputStream fi = new FileInputStream (configFileUrl.getFile());
			FMUwrapperConfig config = (FMUwrapperConfig)xStream.fromXML(fi);
			fi.close();
			
			return config;
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
		
	}
	
	
	public static void make() {
		FMUwrapperConfig config = new FMUwrapperConfig();
		
		XStream xStream = new XStream();
		xStream.alias("config", com.sri.straylight.fmuWrapper.model.FMUwrapperConfig.class);
		
		//serialize
		String xmlStr = xStream.toXML(config);
		URL configFileUrl = ConfigHelper.class.getClassLoader().getResource(configFile);
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
