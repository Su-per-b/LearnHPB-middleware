package com.sri.straylight.socketserver.event;

import java.util.EventObject;

import org.bushe.swing.event.EventBus;


public class MessageReceived extends EventObject {


	private static final long serialVersionUID = 1L;
	private String payload_;
	
	public MessageReceived(Object source, String messageText) {
		super(source);
		
		payload_ = messageText;
	}

    public String getPayload() {
    	
    	return payload_;
    }

	public static void fire(Object source, String str) {
		MessageReceived event = new MessageReceived(source,str);
		EventBus.publish(event);
	}

}
