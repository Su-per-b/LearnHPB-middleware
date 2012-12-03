package com.sri.straylight.socketserver.event;

import com.sri.straylight.fmuWrapper.event.BaseEvent;
import com.sri.straylight.socketserver.StraylightWebSocket;

public class WebSocketConnectionNotify extends BaseEvent<StraylightWebSocket> {

	private static final long serialVersionUID = 1L;
	private StraylightWebSocket payload_;

	public WebSocketConnectionNotify(Object source, StraylightWebSocket payload) {
		super(source);

		payload_ = payload;
	}
	
    public StraylightWebSocket getPayload() {
        return payload_;
    }


    
}
