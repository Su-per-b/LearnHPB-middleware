package com.sri.straylight.socketserver.controller;

import java.util.Properties;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

import com.sri.straylight.common.Banner;
import com.sri.straylight.fmuWrapper.framework.AbstractController;
import com.sri.straylight.socketserver.WebSocketHandlerEx;

public class JettyServerController extends AbstractController{
	
	private Server jettyServer_;
	private WebSocketHandlerEx socketHandler_;
	
	public static Logger logger = Log.getLogger("JettyServerController");
	public static Properties properties;
	private WebSocketConnectionController webSocketConnectionController_;
	
	public JettyServerController (AbstractController parentController ) {
		super(parentController);
	}
	
	public void init() {
		
		showBanner_();
		
		webSocketConnectionController_ = new WebSocketConnectionController(this);
		webSocketConnectionController_.init();
		
	}
	
	private void showBanner_() {
		java.net.URL url = JettyServerController.class.getClassLoader().getResource("SocketServer.properties");
		Banner banner = new Banner(logger, "Socket Server", url);
		banner.show();
	}

	public void start() {
		
		//there is only one of these
		socketHandler_ = new WebSocketHandlerEx();
		try {
			
			jettyServer_ = new Server(8081);
			jettyServer_.setHandler(socketHandler_);
			jettyServer_.start();

			// Jetty server is stopped when the Thread is interrupted.
			jettyServer_.join();

		} catch (Throwable e) {
			e.printStackTrace();
		}
		
	}
	
	
}
 