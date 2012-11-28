package com.sri.straylight.socketserver.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import com.thoughtworks.xstream.XStream;



public class ConfigHelper {

	private static final String configFile = "config.xml";
	

	
	public static SocketServerConfig load() {
		
		String theOs = System.getProperty("os.name");
		System.out.println( "Operating system detected: " + theOs);
		
		
		URL configFileUrl = ConfigHelper.class.getClassLoader().getResource(configFile);

		XStream xStream = new XStream();
		xStream.alias("config", com.sri.straylight.socketserver.model.SocketServerConfig.class);
		
		try {
			FileInputStream fi = new FileInputStream (configFileUrl.getFile());
			SocketServerConfig config = (SocketServerConfig)xStream.fromXML(fi);
			fi.close();
			
			SocketServerConfig.instance = config;
			
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
	

	

	
	
}
