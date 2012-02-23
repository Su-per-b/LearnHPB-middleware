package com.sri.straylight.socketserver;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import com.sri.straylight.common3.Banner;
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
			Server server = new Server(8081);
			
			// 2) Register ChatWebSocketHandler in the Jetty server instance.
			SocketHandlerAll socketHandler = new SocketHandlerAll();
			//socketHandler.setHandler(new DefaultHandler());
			
			server.setHandler(socketHandler);
			

			//SocketHandlerUpload theHandler2 = new SocketHandlerUpload();
			//theHandler2.setHandler(new DefaultHandler());
			//server.setHandler(theHandler2);
			
			
			//ServletContextHandler context0 = new ServletContextHandler(ServletContextHandler.NO_SECURITY | ServletContextHandler.NO_SESSIONS);
			//context0.setContextPath("/");
			
			//String className = SocketHandlerStream.class.getName();
			//context0.addServlet(className,"/*");
			
			//context0.a
			//context0.addServlet(new ServletHolder(socketHandler),"/it/*");
			
			//server.setHandler(context0);
			// 2) Start the Jetty server.
			server.start();
			// Jetty server is stopped when the Thread is interruped.
			server.join();
			
			
			
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	

}
