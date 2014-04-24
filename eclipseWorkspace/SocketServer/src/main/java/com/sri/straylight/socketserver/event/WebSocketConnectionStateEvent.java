package com.sri.straylight.socketserver.event;

import com.sri.straylight.fmuWrapper.event.SerializableEvent;
import com.sri.straylight.socketserver.controller.StrayLightWebSocketHandler;
import com.sri.straylight.socketserver.model.WebSocketConnectionState;


public class WebSocketConnectionStateEvent extends SerializableEvent<WebSocketConnectionState> {
	
	
	private static final long serialVersionUID = 1L;
	
	private StrayLightWebSocketHandler stronglyTypedSource_;
	
	public WebSocketConnectionStateEvent(StrayLightWebSocketHandler source, WebSocketConnectionState payload ) {
		
		super(source, payload);
		stronglyTypedSource_ = source;
	}

	public StrayLightWebSocketHandler getStronglyTypedSource() {
		return stronglyTypedSource_;
	}


    
}
