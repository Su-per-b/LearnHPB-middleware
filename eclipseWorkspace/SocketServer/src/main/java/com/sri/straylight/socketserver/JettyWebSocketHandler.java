package com.sri.straylight.socketserver;



//import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketHandler;

import com.sri.straylight.socketserver.controller.StrayLightWebSocketHandler;



public class JettyWebSocketHandler extends WebSocketHandler {

	public WebSocket doWebSocketConnect(HttpServletRequest request,
			String protocol) {
		
	    String pathInfo = request.getPathInfo();
	    System.out.println("pathInfo: " + pathInfo);
	    
	    if (pathInfo.equals("/")) {
	    	
	    	StrayLightWebSocketHandler socketHandler = new StrayLightWebSocketHandler();
	    	return socketHandler;

	    } else {
	    	return null;
	    }
	}
}