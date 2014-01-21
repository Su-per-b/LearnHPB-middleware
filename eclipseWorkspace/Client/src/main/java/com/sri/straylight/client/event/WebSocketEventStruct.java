package com.sri.straylight.client.event;

import org.apache.commons.lang.builder.EqualsBuilder;

import com.sri.straylight.fmuWrapper.voNative.MessageStruct;
import com.sun.jna.Structure;

/**
 * The Class MessageStruct.
 */
public class WebSocketEventStruct extends Structure   {


	public String eventTitle;
	public String eventDetail;

	/** The message type. */
	public WebSocketEventType eventType;



	@Override
	public boolean equals(Object obj) {

		if (obj == null)
			return false;

		if (obj == this)
			return true;

		if (obj.getClass() != getClass())
			return false;

		MessageStruct struct = (MessageStruct) obj;

		return new EqualsBuilder().append(this.eventTitle, struct.msgText)
				.append(this.eventTitle, struct.messageType).isEquals();
	}
	
	/**
	 * Sets the message type enum.
	 * 
	 * @param theEnum
	 *            the new message type enum
	 */
	public void setMessageTypeEnum(WebSocketEventType webSocketEventType) {
		eventType = webSocketEventType;
	}
	
	
	
}
