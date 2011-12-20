package com.sri.simsvr;



import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.util.log.Log;

/**
 * Hello world!
 *
 */
public class EsimServer 
{
    public static void main( String[] args )
    {
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
			
			
			
		} catch (Throwable e) {
			e.printStackTrace();
		}
        
    }
}
