package com.sri.straylight.socketserver;



import javax.servlet.http.HttpServletRequest;
import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketHandler;


public class WebSocketHandlerEx extends WebSocketHandler {

	private WebSocketConnectionEx webSocketConnection_;
	
	
	public WebSocketConnectionEx getWebSocketConnection(){
		return webSocketConnection_;
	}
	
	public WebSocket doWebSocketConnect(HttpServletRequest request,
			String protocol) {
		
	    String pathInfo = request.getPathInfo();
	    System.out.println("pathInfo: " + pathInfo);
	    
	    if (pathInfo.equals("/")) {
	    	webSocketConnection_ = new WebSocketConnectionEx();
	    	return webSocketConnection_;
	    } else {
	    	return null;
	    }

	}

	
}