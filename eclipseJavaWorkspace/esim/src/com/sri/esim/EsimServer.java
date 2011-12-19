package com.sri.esim;


import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.util.resource.ResourceCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.util.log.Log;

public class EsimServer {
	

	
	public static void main(String[] args) {
		try {
			// 1) Create a Jetty server with the 8091 port.
			Server server = new Server(8080);
			// 2) Register ChatWebSocketHandler in the Jetty server instance.
			EsimHandler chatWebSocketHandler = new EsimHandler();
			chatWebSocketHandler.setHandler(new DefaultHandler());
			server.setHandler(chatWebSocketHandler);
			// 2) Start the Jetty server.
			server.start();
			// Jetty server is stopped when the Thread is interruped.
			server.join();
			
			Server server2 = new Server(80);
			
	        ResourceHandler resource_handler = new ResourceHandler();
	        resource_handler.setDirectoriesListed(true);
	        resource_handler.setWelcomeFiles(new String[]{ "index.html" });

	        resource_handler.setResourceBase("www");
	        Log.info("serving " + resource_handler.getBaseResource());
	        
	        
	        
	        HandlerList handlers = new HandlerList();
	        handlers.setHandlers(new Handler[] { resource_handler, new DefaultHandler() });
	        server2.setHandler(handlers);

	        server2.start();
	        server2.join();
			
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
		

		
		
		
	}
}
