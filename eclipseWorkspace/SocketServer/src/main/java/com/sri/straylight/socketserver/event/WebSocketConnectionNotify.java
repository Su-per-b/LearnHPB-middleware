package com.sri.straylight.socketserver.event;

import com.sri.straylight.fmuWrapper.event.BaseEvent;
import com.sri.straylight.socketserver.controller.StrayLightWebSocketHandler;

public class WebSocketConnectionNotify extends BaseEvent<StrayLightWebSocketHandler> {

	private static final long serialVersionUID = 1L;
//	private StraylightWebSocket payload_;

	public WebSocketConnectionNotify(Object source, StrayLightWebSocketHandler payload) {
		super(source, payload);
	}
	


    
}
