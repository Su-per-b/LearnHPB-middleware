package com.sri.simsvr2;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.util.log.Log;

public class PageServer {
	

	public static void main(String[] args) {
		try {

			Server server = new Server(80);
			
	        ResourceHandler resource_handler = new ResourceHandler();
	        resource_handler.setDirectoriesListed(true);
	        resource_handler.setWelcomeFiles(new String[]{ "index.html" });

	        resource_handler.setResourceBase("html");
	        Log.info("serving " + resource_handler.getBaseResource());
	        
	        HandlerList handlers = new HandlerList();
	        handlers.setHandlers(new Handler[] { resource_handler, new DefaultHandler() });
	        server.setHandler(handlers);

	        server.start();
	        server.join();
			
		} catch (Throwable e) {
			e.printStackTrace();
		}

	}
}
