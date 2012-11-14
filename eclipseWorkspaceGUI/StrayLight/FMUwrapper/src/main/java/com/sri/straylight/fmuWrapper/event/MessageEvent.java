
package com.sri.straylight.fmuWrapper.event;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

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
    
    
    public int hashCode() {
        return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
            append(this.source).
            append(this.payload_).
            toHashCode();
    }

    
    public boolean equals(Object obj) {
    	
        if (obj == null)
            return false;
        
        if (obj == this)
            return true;
        
        if (obj.getClass() != getClass())
            return false;

        MessageEvent messageEvent = (MessageEvent) obj;
        
        return new EqualsBuilder().
            // if deriving: appendSuper(super.equals(obj)).
            append(this.source, messageEvent.source).
            append(this.payload_, messageEvent.payload_).
            isEquals();
    }
    
    

    
    

      
    
}


