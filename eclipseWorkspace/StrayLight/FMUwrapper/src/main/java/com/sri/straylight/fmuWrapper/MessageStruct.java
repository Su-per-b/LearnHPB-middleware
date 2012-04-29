package com.sri.straylight.fmuWrapper;


import com.sun.jna.Structure;

public class MessageStruct extends Structure {

	public String msgText;
	public int messageType;


	public MessageType getMessageTypeEnum() {

		MessageType theEnum = MessageType.messageType_debug;
		theEnum  = theEnum.getForValue (messageType);
		
		return theEnum;
	}
	
}
