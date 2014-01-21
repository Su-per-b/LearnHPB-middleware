package com.sri.straylight.socketserver.controller;

import java.util.Properties;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

import com.sri.straylight.common.Banner;
import com.sri.straylight.fmuWrapper.framework.AbstractController;
import com.sri.straylight.socketserver.JettyWebSocketHandler;

public class JettyServerController extends AbstractController {
	
	private Server jettyServer_;
	private JettyWebSocketHandler jettyWebSocketHandler_;
	
	public static Logger logger = Log.getLogger("JettyServerController");
	public static Properties properties;
	
//	private WebSocketConnectionController webSocketConnectionController_;
	
	public JettyServerController (AbstractController parentController ) {
		super(parentController);
	}
	
	public void init() {
		showBanner_();
		start();
	}
	
	private void showBanner_() {
		java.net.URL url = JettyServerController.class.getClassLoader().getResource("SocketServer.properties");
		Banner banner = new Banner(logger, "Socket Server", url);
		banner.show();
	}

	public void stop() {
		
		try {
			jettyWebSocketHandler_.stop();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		try {
			jettyServer_.stop();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	public void start() {
		
		//there is only one of these

		
	//	jettyWebSocketHandler_.setTheParent();
		
		try {
			
			
			
			jettyServer_ = new Server(8081);
			
			jettyWebSocketHandler_ = new JettyWebSocketHandler();
			
			SessionHandler sh = new SessionHandler();
			sh.setHandler(jettyWebSocketHandler_);   
			
				
			jettyServer_.setHandler(sh);
			jettyServer_.start();

			// Jetty server is stopped when the Thread is interrupted.
			jettyServer_.join();

		} catch (Throwable e) {
			e.printStackTrace();
		}
		
	}
	
	
}
 