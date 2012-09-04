
package com.sri.straylight.fmuWrapper.event;

import java.util.EventObject;

import com.sri.straylight.fmuWrapper.voNative.MessageStruct;
import com.sri.straylight.fmuWrapper.voNative.MessageType;





// TODO: Auto-generated Javadoc
/**
 * The Class MessageEvent.
 */
public class MessageEvent extends EventObject {


	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The message struct. */
	public MessageStruct messageStruct;
	
    //here's the constructor
    /**
     * Instantiates a new message event.
     *
     * @param source the source
     */
    public MessageEvent(Object source) {
        super(source);
        
    }
    
    /**
     * Instantiates a new message event.
     *
     * @param source the source
     * @param msgText the msg text
     * @param messageType the message type
     */
    public MessageEvent(Object source, String msgText, MessageType messageType) {
        super(source);
        
        messageStruct = new MessageStruct();
    	messageStruct.msgText = msgText;
    	messageStruct.setMessageTypeEnum(messageType);
        
    }
    
}


