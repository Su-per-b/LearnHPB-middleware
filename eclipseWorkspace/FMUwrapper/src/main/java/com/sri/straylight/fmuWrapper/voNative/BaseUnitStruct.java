package com.sri.straylight.fmuWrapper.voNative;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.sri.straylight.fmuWrapper.serialization.Iserializable;
import com.sri.straylight.fmuWrapper.serialization.JsonController;
import com.sun.jna.Structure;


/**
 * The Class MessageStruct.
 */
public class BaseUnitStruct extends Structure implements Iserializable {

	/** The msg text. */
	public String msgText;

	/** The message type. */
	public int messageType;

	private boolean serializeType_ = true;
	
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
	

	public String serialize() {
		return JsonController.getInstance().serialize(this);
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

        BaseUnitStruct struct = (BaseUnitStruct) obj;
        
        return new EqualsBuilder().
            append(this.msgText, struct.msgText).
            append(this.messageType, struct.messageType).
            isEquals();
    }
    
    
	public boolean getSerializeType() {
		return serializeType_;
	}
	
	public void setSerializeType(boolean serializeType) {
		serializeType_ = serializeType;
	}
	
	@Override
	protected List<String> getFieldOrder() {
	    return Arrays.asList(new String[] { "msgText", "messageType" });
	}
	
	
}
