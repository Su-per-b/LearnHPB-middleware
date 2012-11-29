package com.sri.straylight.socketserver.event;

import java.util.EventObject;

import org.bushe.swing.event.EventBus;

import com.sri.straylight.socketserver.model.WebSocketConnectionState;


public class WebSocketConnectionStateEvent extends EventObject {
	
	
	private static final long serialVersionUID = 1L;
	private WebSocketConnectionState payload_;
	
	public WebSocketConnectionStateEvent(Object source, WebSocketConnectionState payload) {
		super(source);
		
		payload_ = payload;
	}
	

    public WebSocketConnectionState getPayload() {
    	return payload_;
    }



	public static void fire(Object source, WebSocketConnectionState state) {
		WebSocketConnectionStateEvent event = new WebSocketConnectionStateEvent(source,state);
		EventBus.publish(event);
	}
    
}
