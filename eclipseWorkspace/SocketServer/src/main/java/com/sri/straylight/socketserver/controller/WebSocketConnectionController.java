package com.sri.straylight.socketserver.controller;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.sri.straylight.fmuWrapper.event.BaseEvent;
import com.sri.straylight.fmuWrapper.event.SimStateClientNotify;
import com.sri.straylight.fmuWrapper.event.SimStateNativeNotify;
import com.sri.straylight.fmuWrapper.framework.AbstractController;
import com.sri.straylight.fmuWrapper.serialization.JsonController;
import com.sri.straylight.fmuWrapper.serialization.JsonSerializable;
import com.sri.straylight.socketserver.WebSocketConnectionEx;
import com.sri.straylight.socketserver.WebSocketHandlerEx;
import com.sri.straylight.socketserver.event.MessageReceived;

public class WebSocketConnectionController extends AbstractController  {
	
//	private WebSocketHandlerEx socketHandler_;
	private WebSocketConnectionEx webSocketConnection_;
	
	
	public WebSocketConnectionController(AbstractController parentController) {
		super(parentController);
	}
	
	
	public void init() {

	}
	
//	
//	public WebSocketHandlerEx getSocketHandler() {
//		return socketHandler_;
//	}
//
//
//	
//	public void init() {
//		socketHandler_ = new WebSocketHandlerEx();
//	}
//	
	
	//message has been received from the client
	@EventSubscriber(eventClass=MessageReceived.class)
	public void onMessageReceived(MessageReceived event) {
		
		String messageText = event.getPayload();
    	JsonSerializable deserializedObject = JsonController.getInstance().fromJson(messageText);
    	
    	//if it is an event then just publish it
    	if (deserializedObject instanceof BaseEvent) {
    		EventBus.publish(deserializedObject);
    	}
	}
	
	@EventSubscriber(eventClass = SimStateNativeNotify.class)
	public void onSimStateNativeNotify(SimStateNativeNotify event) {

		send (
			new SimStateClientNotify(this, event.getPayload())
		);

	}


	private void send(BaseEvent<?> event) {
		webSocketConnection_.serializeAndSend(event);
	}

	public void setWebSocketConnection(WebSocketConnectionEx webSocketConnection) {
		webSocketConnection_ = webSocketConnection;
	}
	
	public WebSocketConnectionEx getWebSocketConnection() {
		return webSocketConnection_;
	}
//	public WebSocketConnectionEx getWebSocketHandler() {
//		return webSocketConnection_.
//	}
//	





	
}
