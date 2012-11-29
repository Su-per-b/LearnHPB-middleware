package com.sri.straylight.socketserver.event;

import com.sri.straylight.fmuWrapper.event.BaseEvent;
import com.sri.straylight.socketserver.WebSocketConnectionEx;

public class WebSocketConnectionNotify extends BaseEvent<WebSocketConnectionEx> {

	private static final long serialVersionUID = 1L;
	private WebSocketConnectionEx payload_;

	public WebSocketConnectionNotify(Object source, WebSocketConnectionEx payload) {
		super(source);

		payload_ = payload;
	}
	
    public WebSocketConnectionEx getPayload() {
        return payload_;
    }


    
}
