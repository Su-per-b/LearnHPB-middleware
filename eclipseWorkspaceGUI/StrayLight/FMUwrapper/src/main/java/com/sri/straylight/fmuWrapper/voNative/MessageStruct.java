package com.sri.straylight.fmuWrapper.voNative;

import com.sri.straylight.fmuWrapper.serialization.JsonController;
import com.sri.straylight.fmuWrapper.serialization.JsonSerializable;
import com.sun.jna.Structure;
import org.apache.commons.lang.builder.EqualsBuilder;

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

	
	


}
