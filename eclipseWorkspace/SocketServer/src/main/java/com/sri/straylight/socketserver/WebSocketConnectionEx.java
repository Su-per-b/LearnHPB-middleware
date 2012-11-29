package com.sri.straylight.socketserver;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.eclipse.jetty.websocket.WebSocket;

import com.sri.straylight.fmuWrapper.serialization.JsonSerializable;
import com.sri.straylight.socketserver.event.MessageReceived;
import com.sri.straylight.socketserver.event.WebSocketConnectionNotify;
import com.sri.straylight.socketserver.event.WebSocketConnectionStateEvent;
import com.sri.straylight.socketserver.model.WebSocketConnectionState;


public class WebSocketConnectionEx implements WebSocket.OnTextMessage, WebSocket.OnControl  {

	private Connection connection_;
	private final Set<WebSocketConnectionEx> webSocketsList_ = new CopyOnWriteArraySet<WebSocketConnectionEx>();
	private WebSocketConnectionState webSocketConnectionState_ = WebSocketConnectionState.uninitialized;

    public WebSocketConnectionEx() {
    	AnnotationProcessor.process(this);
    }
    
    
    //handle connection open
	public void onOpen(Connection connection) {
		
		// A client has opened a connection with us
		// Store the opened connection
		this.connection_ = connection;
		
		//Add new WebSocketConnection in the list
		webSocketsList_.add(this);
		//setState(WebSocketConnectionState.opened_new);
		
		new WebSocketConnectionNotify(this, this).fire();
		

	}
	
	


	//handle incoming message
	public void onMessage(String str) {
		 
		System.out.println("SocketHandlerStream.onMessage " + str);
		MessageReceived.fire(this, str);

	}
	
	//handle control code
	public boolean onControl(byte controlCode,byte[] data, int offset, int length) {
		
		
		return true;
	}
	
	//handle connection close
	public void onClose(int closeCode, String message) {
		
		// Remove WebSocket from the list of WebSockets
		webSocketsList_.remove(this);

	}
	
	
	private void setState(WebSocketConnectionState state) {
		webSocketConnectionState_ = state;
		WebSocketConnectionStateEvent.fire(this, state);
	}

	public void serializeAndSend(JsonSerializable obj) {
		sendString_ (obj.toJson());
	}
	
	public void sendString_(String str) {

		try {
			this.connection_.sendMessage(str);
		} catch (IOException e) {
			e.printStackTrace();
			//	this.connection_.disconnect();
		}
	}
	
	public void sendStringToAll_(String str) {
		
		// loop though all clients
		for (WebSocketConnectionEx webSocket : webSocketsList_) {
			webSocket.sendString_(str);
		}

	}






	
}