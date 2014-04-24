package com.sri.straylight.socketserver.controller;

import java.io.IOException;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.LogFactory;
import org.bushe.swing.event.EventBus;
import org.eclipse.jetty.websocket.WebSocket;

import com.sri.straylight.fmuWrapper.event.XMLparsedEvent;
import com.sri.straylight.fmuWrapper.serialization.Iserializable;
import com.sri.straylight.fmuWrapper.serialization.JsonController;
import com.sri.straylight.fmuWrapper.voManaged.XMLparsedInfo;
import com.sri.straylight.socketserver.event.WebSocketConnectionStateEvent;
import com.sri.straylight.socketserver.model.WebSocketConnectionState;


public class StrayLightWebSocketHandler
	implements WebSocket.OnTextMessage, WebSocket.OnControl, Iserializable  {

	private static int socketCount_ = 0;
	
	@SuppressWarnings("unused")
	private int socketID_ = 0;
	
	private String sessionID_;
	private Connection connection_;
	private WebSocketConnectionState state_ = WebSocketConnectionState.uninitialized;
	
	private int idx_ = -1;
	private WebSocketConnectionController parentController_;
	private String messageQueItem_;
	private HttpSession httpSession_;
	
	
	public StrayLightWebSocketHandler(HttpSession httpSession) {

		socketID_ = socketCount_;
		socketCount_++;
		httpSession_ = httpSession;
		sessionID_ = httpSession_.getId();
	}
	
	
	public void setIdx(int idx) {
		idx_ = idx;
	}
	
	
	public String getSessionID() {
		return httpSession_.getId();
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
		
    	System.out.println("StrayLightWebSocketHandler.onOpen() sessionID: " + sessionID_);
    	
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
		
		System.out.println("StrayLightWebSocketHandler.onControl() controlCode: " + controlCode);
		
		setState(WebSocketConnectionState.closeRequested);
		
		if (8 == controlCode) {
			
			
		}
		
		
		return true;
	}
	
	
	//handle connection closc 
	
	public void onClose(int closeCode, String message) {
		

		setState(WebSocketConnectionState.closed);
    	System.out.println("StrayLightWebSocketHandler.onClose() sessionID: " + sessionID_);
	}
	
	
	private void setState(WebSocketConnectionState state) {
		state_ = state;
		WebSocketConnectionStateEvent e = new WebSocketConnectionStateEvent(this, state);
		EventBus.publish(e);
	}
	
	
	
	public void serializeAndSend(Iserializable obj) {
		
		
		if (obj instanceof XMLparsedEvent) {
			
			XMLparsedEvent event = (XMLparsedEvent) obj;
			
			XMLparsedInfo  xmlParsedInfo = event.getPayload();
			xmlParsedInfo.setSessionID(sessionID_);
		}
		  
		String json = obj.toJsonString();
		sendString_ (json);
	}
	
	
	public void sendString_(String str) {
		
		LogFactory.getLog(this.getClass()).debug("sendString_ " + str);
		
		
		//System.out.println("StrayLightWebSocketHandler.sendString_() sessionID_: " + sessionID_);
		
		
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


    public String toJsonString() {
    	return JsonController.getInstance().toJsonString(this);
    }


	
}