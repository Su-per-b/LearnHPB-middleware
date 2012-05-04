package com.sri.straylight.socketserver;



import javax.servlet.http.HttpServletRequest;
import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketHandler;


public class SocketHandler extends WebSocketHandler {



	public WebSocket doWebSocketConnect(HttpServletRequest request,
			String protocol) {
		
	    System.out.println("WebSocket.doWebSocketConnect");
	    
	    String pathInfo = request.getPathInfo();
	    System.out.println("pathInfo: " + pathInfo);
	    
	    if (pathInfo.equals("/")) {
	    	return new WebSocketListener();
	    } else {
	    	return null;
	    }

	}

	
}