package com.sri.straylight.socketserver.event;

import com.sri.straylight.fmuWrapper.event.BaseEvent;
import com.sri.straylight.socketserver.controller.SocketController;

public class WebSocketConnectionNotify extends BaseEvent<SocketController> {

	private static final long serialVersionUID = 1L;
//	private StraylightWebSocket payload_;

	public WebSocketConnectionNotify(Object source, SocketController payload) {
		super(source, payload);

	}
	


    
}
