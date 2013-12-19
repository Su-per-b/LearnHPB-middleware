package com.sri.straylight.socketserver;



//import java.util.ArrayList;


import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketHandler;

import com.sri.straylight.socketserver.controller.StrayLightWebSocketHandler;



public class JettyWebSocketHandler extends WebSocketHandler {

	private HashMap<String, StrayLightWebSocketHandler> handerMap_ = 
			new HashMap<String, StrayLightWebSocketHandler>();
	
	
	
	public WebSocket doWebSocketConnect(HttpServletRequest request, String protocol) {
		
	    String pathInfo = request.getPathInfo();
	    System.out.println("pathInfo: " + pathInfo);
	    //StrayLightWebSocketHandler

	    
	    if (pathInfo.equals("/")) {

	    	HttpSession httpSession = request.getSession();
	    	String sessionID = httpSession.getId();
	    	
	    	System.out.println("WebSocket.doWebSocketConnect() sessionID: " + sessionID);
	    	
	    	StrayLightWebSocketHandler socketHandler = new StrayLightWebSocketHandler(httpSession);

	    	
	    	handerMap_.put(sessionID, socketHandler);
	    	
	    	
	    	return socketHandler;

	    } else {
	    	return null;
	    }
	}
}