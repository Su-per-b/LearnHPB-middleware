package com.sri.straylight.socketserver;



import javax.servlet.http.HttpServletRequest;
import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketHandler;


public class SocketHandlerAll extends WebSocketHandler {



	public WebSocket doWebSocketConnect(HttpServletRequest request,
			String protocol) {
		
	    System.out.println("WebSocket.doWebSocketConnect");
	    

	    
	    String pathInfo = request.getPathInfo();
	    System.out.println("pathInfo: " + pathInfo);
	    
	   
	    
	  //  if (pathInfo.equals("/stream")) {
	    	return new WebSocketStream();
	  //  } else {
	    	//return null;
	   // }

	}

	
}