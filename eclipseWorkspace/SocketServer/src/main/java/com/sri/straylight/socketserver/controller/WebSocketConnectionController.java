package com.sri.straylight.socketserver.controller;

import com.sri.straylight.fmuWrapper.event.BaseEvent;
import com.sri.straylight.fmuWrapper.event.SimStateNativeRequest;
import com.sri.straylight.fmuWrapper.event.StraylightEventListener;
import com.sri.straylight.fmuWrapper.framework.AbstractController;
import com.sri.straylight.fmuWrapper.serialization.JsonController;
import com.sri.straylight.fmuWrapper.serialization.JsonSerializable;
import com.sri.straylight.socketserver.event.MessageReceived;

public class WebSocketConnectionController 
	extends AbstractController  
	{
	
	private StrayLightWebSocketHandler socketHandler_;

	
	public WebSocketConnectionController(ConnectionBundle parent, StrayLightWebSocketHandler socketHandler) {
		super(parent);
		
		socketHandler_ = socketHandler;
	}


	public void init() {
		socketHandler_.setParentController(this);
		
//		socketController_.registerEventListener(MessageReceived.class,this);
	}
	
	
	public void processQuedItem() {
		
		
		socketHandler_.processQuedItem();
	}
	
	
	
	public void onMessageRecieved(String str) {
		
		System.out.println("SocketHandlerStream.onMessage " + str);
		MessageReceived e = new MessageReceived(this, str);
		
		fireEvent(e);
	}
	
//
//	@Override
//	public void handleEvent(MessageReceived event) {
//		
//		String messageText = event.getPayload();
//    	JsonSerializable deserializedEvent = JsonController.getInstance().fromJson(messageText);
//    	
//    	//if it is an event then just publish it
//    	if (deserializedEvent instanceof SimStateNativeRequest) {
//    		SimStateNativeRequest newEvent = (SimStateNativeRequest) deserializedEvent;
//    		this.fireEvent(newEvent);
//    	}
//    	
//	}
	

	public void send(BaseEvent<?> event) {
		socketHandler_.serializeAndSend(event);
	}

	public void setStraylightWebSocket(StrayLightWebSocketHandler socketHandler) {
		socketHandler_ = socketHandler;
	}
	
	public StrayLightWebSocketHandler getStraylightWebSocket() {
		return socketHandler_;
	}






	
}
