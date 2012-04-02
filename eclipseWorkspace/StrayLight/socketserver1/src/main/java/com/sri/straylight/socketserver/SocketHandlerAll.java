package com.sri.straylight.socketserver;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketHandler;
import java.text.DecimalFormat;

public class SocketHandlerAll extends WebSocketHandler {



	public WebSocket doWebSocketConnect(HttpServletRequest request,
			String protocol) {
		
	    System.out.println("doWebSocketConnect");
	    

	    
	    String pathInfo = request.getPathInfo();
	    System.out.println("pathInfo: " + pathInfo);
	    
	   
	    
	    if (pathInfo.equals("/stream")) {
	    	return new WebSocketStream();
	    } else {
	    	return null;
	    }
		
		
		
	}

	
}