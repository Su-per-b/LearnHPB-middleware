package com.sri.straylight.socketserver;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.eclipse.jetty.websocket.WebSocket;

import com.sri.straylight.fmuWrapper.event.SimStateNativeNotify;
import com.sri.straylight.fmuWrapper.framework.AbstractController;
import com.sri.straylight.fmuWrapper.serialization.JsonSerializable;
import com.sri.straylight.fmuWrapper.voNative.SimStateNative;
import com.sri.straylight.socketserver.event.MessageReceived;
import com.sri.straylight.socketserver.event.WebSocketConnectionNotify;
import com.sri.straylight.socketserver.event.WebSocketConnectionStateEvent;
import com.sri.straylight.socketserver.model.WebSocketConnectionState;


public class StraylightWebSocket extends AbstractController 
	implements WebSocket.OnTextMessage, WebSocket.OnControl  {

	private Connection connection_;
	private final Set<StraylightWebSocket> webSocketsList_ = new CopyOnWriteArraySet<StraylightWebSocket>();
	private WebSocketConnectionState webSocketConnectionState_ = WebSocketConnectionState.uninitialized;
	
	private int idx_;
	
//    public StraylightWebSocket(AbstractController parent) {
//    	super(parent);
//    }
    
    
    //handle connection open
	public void onOpen(Connection connection) {
		
		// A client has opened a connection with us
		// Store the opened connection
		this.connection_ = connection;
		
		//Add new WebSocketConnection in the list
		webSocketsList_.add(this);
		new WebSocketConnectionNotify(this, this).fire();
		
	}
	
	


	//handle incoming message
	public void onMessage(String str) {
		 
		System.out.println("SocketHandlerStream.onMessage " + str);
		
		MessageReceived e = new MessageReceived(this, str);
		
		this.fireEvent(e);

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
	
	
	public void setState(WebSocketConnectionState state) {
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
		for (StraylightWebSocket webSocket : webSocketsList_) {
			webSocket.sendString_(str);
		}
	}


	public void setIdx(int idx) {
		idx_ = idx;
	}






	
}