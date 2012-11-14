package com.sri.straylight.fmuWrapper.voNative;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.sri.straylight.fmuWrapper.event.MessageEvent;
import com.sri.straylight.fmuWrapper.serialization.JsonController;
import com.sri.straylight.fmuWrapper.serialization.JsonSerializable;
import com.sun.jna.Structure;

// TODO: Auto-generated Javadoc
/**
 * The Class MessageStruct.
 */
public class MessageStruct extends Structure implements JsonSerializable {

	/** The msg text. */
	public String msgText;

	/** The message type. */
	public int messageType;

	/**
	 * Gets the message type enum.
	 * 
	 * @return the message type enum
	 */
	public MessageType getMessageTypeEnum() {

		MessageType theEnum = MessageType.messageType_debug;
		theEnum = theEnum.getForValue(messageType);

		return theEnum;
	}

	/**
	 * Sets the message type enum.
	 * 
	 * @param theEnum
	 *            the new message type enum
	 */
	public void setMessageTypeEnum(MessageType theEnum) {
		messageType = theEnum.getIntValue();
	}
	

	public String toJson() {
		return JsonController.getInstance().toJson(this);
	}

	@Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
            append(this.msgText).
            append(this.messageType).
            toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
    	
        if (obj == null)
            return false;
        
        if (obj == this)
            return true;
        
        if (obj.getClass() != getClass())
            return false;

        MessageStruct struct = (MessageStruct) obj;
        
        return new EqualsBuilder().
            append(this.msgText, struct.msgText).
            append(this.messageType, struct.messageType).
            isEquals();
    }
}
