package com.sri.straylight.fmuWrapper;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

import com.thoughtworks.xstream.XStream;



public class ConfigHelper {

	private static final String configFile = "config.xml";
	

	
	public static Config load() {
		
		String theOs = System.getProperty("os.name");
		System.out.println( "Operating system detected: " + theOs);
		
		
		URL configFileUrl = ConfigHelper.class.getClassLoader().getResource(configFile);

		XStream xStream = new XStream();
		xStream.alias("config", com.sri.straylight.fmuWrapper.Config.class);
		
		try {
			FileInputStream fi = new FileInputStream (configFileUrl.getFile());
			Config config = (Config)xStream.fromXML(fi);
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
		Config config = new Config();
		
		XStream xStream = new XStream();
		xStream.alias("config", com.sri.straylight.fmuWrapper.Config.class);
		
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