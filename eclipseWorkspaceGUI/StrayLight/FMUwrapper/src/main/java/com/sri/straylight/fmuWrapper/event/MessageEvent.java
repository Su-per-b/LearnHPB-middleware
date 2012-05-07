
package com.sri.straylight.fmuWrapper.event;

import java.util.EventObject;

import com.sri.straylight.fmuWrapper.voNative.MessageStruct;
import com.sri.straylight.fmuWrapper.voNative.MessageType;





public class MessageEvent extends EventObject {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public MessageStruct messageStruct;
	
    //here's the constructor
    public MessageEvent(Object source) {
        super(source);
        
    }
    
    public MessageEvent(Object source, String msgText, MessageType messageType) {
        super(source);
        
        messageStruct = new MessageStruct();
    	messageStruct.msgText = msgText;
    	messageStruct.setMessageTypeEnum(messageType);
        
    }
    
}


