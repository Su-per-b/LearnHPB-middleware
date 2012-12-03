package com.sri.straylight.socketserver.event;

import org.bushe.swing.event.EventBus;

import com.sri.straylight.fmuWrapper.event.BaseEvent;


public class MessageReceived extends BaseEvent<String> {


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