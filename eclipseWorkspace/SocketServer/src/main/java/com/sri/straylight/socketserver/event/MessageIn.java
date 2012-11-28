package com.sri.straylight.socketserver.event;

import java.util.EventObject;

public class MessageIn extends EventObject {


	private static final long serialVersionUID = 1L;
	private String payload_;
	
	public MessageIn(Object source, String messageText) {
		super(source);
		
		payload_ = messageText;
		

	}
	
    /**
     * Gets the payload.
     *
     * @return the payload
     */
    public String getPayload() {
    	
    	return payload_;
    }

}
