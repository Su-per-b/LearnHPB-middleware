package com.sri.straylight.socketserver.controller;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.sri.straylight.fmuWrapper.event.BaseEvent;
import com.sri.straylight.fmuWrapper.event.SimStateClientNotify;
import com.sri.straylight.fmuWrapper.event.SimStateNativeNotify;
import com.sri.straylight.fmuWrapper.event.SimStateNativeRequest;
import com.sri.straylight.fmuWrapper.event.StraylightEventListener;
import com.sri.straylight.fmuWrapper.framework.AbstractController;
import com.sri.straylight.fmuWrapper.serialization.JsonController;
import com.sri.straylight.fmuWrapper.serialization.JsonSerializable;
import com.sri.straylight.socketserver.StraylightWebSocket;
import com.sri.straylight.socketserver.JettyWebSocketHandler;
import com.sri.straylight.socketserver.event.MessageReceived;

public class WebSocketConnectionController 
	extends AbstractController  
	implements StraylightEventListener<MessageReceived, String>
	{
	
//	private WebSocketHandlerEx socketHandler_;
	private StraylightWebSocket straylightWebSocket_;
	private JettyWebSocketHandler socketHandler_;
	
	private int idx_;
	
//	public WebSocketConnectionController(AbstractController parent) {
//		super(parent);
//	}
	
	
	public WebSocketConnectionController(ConnectionBundle parent, StraylightWebSocket straylightWebSocket, int idx) {
		super(parent);
		idx_ = idx;
		
		straylightWebSocket_ = straylightWebSocket;
	}


	public void init() {
		straylightWebSocket_.setParentController(this);
		
		straylightWebSocket_.registerEventListener(MessageReceived.class,this);
	}
	

	@Override
	public void handleEvent(MessageReceived event) {
		
		String messageText = event.getPayload();
    	JsonSerializable deserializedEvent = JsonController.getInstance().fromJson(messageText);
    	
    	//if it is an event then just publish it
    	if (deserializedEvent instanceof SimStateNativeRequest) {
    		
    		SimStateNativeRequest newEvent = (SimStateNativeRequest) deserializedEvent;
    				
    		this.fireEvent(newEvent);
    	}
    	
	}
	



	public void send(BaseEvent event) {
		straylightWebSocket_.serializeAndSend(event);
	}

	public void setStraylightWebSocket(StraylightWebSocket webSocketConnection) {
		straylightWebSocket_ = webSocketConnection;
	}
	
	public StraylightWebSocket getStraylightWebSocket() {
		return straylightWebSocket_;
	}






	
}
