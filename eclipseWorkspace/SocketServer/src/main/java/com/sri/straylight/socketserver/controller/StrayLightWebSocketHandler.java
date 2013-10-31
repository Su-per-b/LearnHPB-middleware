package com.sri.straylight.socketserver.controller;

import java.io.IOException;

import org.apache.commons.logging.LogFactory;
import org.bushe.swing.event.EventBus;
import org.eclipse.jetty.websocket.WebSocket;

import com.sri.straylight.fmuWrapper.serialization.JsonSerializable;
import com.sri.straylight.socketserver.event.WebSocketConnectionStateEvent;
import com.sri.straylight.socketserver.model.WebSocketConnectionState;


public class StrayLightWebSocketHandler
	implements WebSocket.OnTextMessage, WebSocket.OnControl  {

	private Connection connection_;
	private WebSocketConnectionState state_ = WebSocketConnectionState.uninitialized;
	
	private int idx_ = -1;
	private WebSocketConnectionController parentController_;
	private String messageQueItem_;
	
	
	public StrayLightWebSocketHandler() {
		
	}
	
	
	public void setIdx(int idx) {
		idx_ = idx;
	}
	
	
	public int getIdx() {
		return idx_;
	}
	
	public WebSocketConnectionState getState() {
		return state_;
	}
	

	
    //handle connection open
	public void onOpen(Connection connection) {
		
		// A client has opened a connection with us
		// Store the opened connection
		this.connection_ = connection;
		setState(WebSocketConnectionState.opened_new);
		return;
	}
	
	


	//handle incoming message
	public void onMessage(String str) {
		
		if (null == parentController_) {
			messageQueItem_ = str;
		} else {
			parentController_.onMessageRecieved(str);
		}
		
	}
	
	
	
	//handle control code
	public boolean onControl(byte controlCode,byte[] data, int offset, int length) {
		
		
		return true;
	}
	
	
	//handle connection closc 
	
	public void onClose(int closeCode, String message) {
		
		setState(WebSocketConnectionState.closed);
	}
	
	
	private void setState(WebSocketConnectionState state) {
		state_ = state;
		WebSocketConnectionStateEvent e = new WebSocketConnectionStateEvent(this, state);
		EventBus.publish(e);
	}
	
	
	
	public void serializeAndSend(JsonSerializable obj) {
		String json = obj.toJson();
		sendString_ (json);
	}
	
	
	private void sendString_(String str) {
		
		LogFactory.getLog(this.getClass()).debug("sendString_ " + str);
		
		try {
			if (this.connection_.isOpen()) {
				this.connection_.sendMessage(str);
			}

		} catch (IOException e) {
			e.printStackTrace();
			//	this.connection_.disconnect();
		}
	}


	public void setParentController(WebSocketConnectionController webSocketConnectionController) {
		parentController_ = webSocketConnectionController;
	}


	public void processQuedItem() {
		if (null != messageQueItem_) {
			parentController_.onMessageRecieved(messageQueItem_);
		}
	}





	
}