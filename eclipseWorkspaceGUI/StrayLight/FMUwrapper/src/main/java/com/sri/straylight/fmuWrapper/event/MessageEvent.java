
package com.sri.straylight.fmuWrapper.event;

import com.sri.straylight.fmuWrapper.voNative.MessageStruct;
import com.sri.straylight.fmuWrapper.voNative.MessageType;





// TODO: Auto-generated Javadoc
/**
 * The Class MessageEvent.
 */
public class MessageEvent extends BaseEvent<MessageStruct> {



    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;




	/**
     * Instantiates a new message event.
     *
     * @param source the source
     */
    public MessageEvent(Object source, MessageStruct payload) {
        super(source);
        setPayload( payload );
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

        MessageStruct payload = new MessageStruct();
        payload.msgText = msgText;
        payload.setMessageTypeEnum(messageType);
    	
        setPayload( payload );
    }
    
    

    
    
}


