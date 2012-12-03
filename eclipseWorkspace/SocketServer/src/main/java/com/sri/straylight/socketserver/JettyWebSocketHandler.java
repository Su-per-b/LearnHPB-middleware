package com.sri.straylight.socketserver;



import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketHandler;


public class JettyWebSocketHandler extends WebSocketHandler {

	private StraylightWebSocket straylightWebSocket_;
//	private AbstractController parent_;

	
//	public JettyWebSocketHandler(AbstractController parent) {
//		super();
//		parent_ = parent;
//	}
	
	
	public StraylightWebSocket getStraylightWebSocket(){
		return straylightWebSocket_;
	}
	
	public WebSocket doWebSocketConnect(HttpServletRequest request,
			String protocol) {
		
	    String pathInfo = request.getPathInfo();
	    System.out.println("pathInfo: " + pathInfo);
	    
	    if (pathInfo.equals("/")) {
	    	straylightWebSocket_ = new StraylightWebSocket();
	    	return straylightWebSocket_;
	    } else {
	    	return null;
	    }

	}
}