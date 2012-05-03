
package com.sri.straylight.fmuWrapper.event;

import java.util.EventObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sri.straylight.fmuWrapper.MessageStruct;
import com.sri.straylight.fmuWrapper.MessageType;



public class MessageEvent extends EventObject {


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


