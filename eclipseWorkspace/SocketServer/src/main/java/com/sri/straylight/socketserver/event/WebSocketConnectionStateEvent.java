package com.sri.straylight.socketserver.event;

import com.sri.straylight.fmuWrapper.event.BaseEvent;
import com.sri.straylight.socketserver.controller.SocketController;
import com.sri.straylight.socketserver.model.WebSocketConnectionState;


public class WebSocketConnectionStateEvent extends BaseEvent<WebSocketConnectionState> {
	
	
	private static final long serialVersionUID = 1L;
	
	private SocketController stronglyTypedSource_;
	
	public WebSocketConnectionStateEvent(SocketController source, WebSocketConnectionState payload ) {
		
		super(source, payload);
		stronglyTypedSource_ = source;
	}

	public SocketController getStronglyTypedSource() {
		return stronglyTypedSource_;
	}






    
}
