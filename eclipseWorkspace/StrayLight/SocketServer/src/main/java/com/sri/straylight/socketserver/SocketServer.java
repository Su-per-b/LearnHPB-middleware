package com.sri.straylight.socketserver;

import java.net.URL;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import com.sri.straylight.common.Banner;
import java.util.Properties;

public class SocketServer {

	public static Logger logger = Log.getLogger("SocketServer");
	public static Properties properties;
	
	
	public void showBanner() {
			
		java.net.URL url = SocketServer.class.getClassLoader().getResource("SocketServer.properties");
		
		Banner banner = new Banner(logger, "Socket Server", url);
		banner.show();
	}
	
	public void start() {

		try {
			// 1) Create a Jetty server with the 8080 port.
			Server server = new Server(8080);
			
			// 2) Register ChatWebSocketHandler in the Jetty server instance.
			SocketHandler theHandler = new SocketHandler();
			theHandler.setHandler(new DefaultHandler());
			server.setHandler(theHandler);
			
			// 2) Start the Jetty server.
			server.start();
			// Jetty server is stopped when the Thread is interruped.
			server.join();
			
			
			
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	

}
