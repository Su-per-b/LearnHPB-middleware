package com.sri.straylight.fmuWrapper.voNative;




import com.sun.jna.Structure;


// TODO: Auto-generated Javadoc
/**
 * The Class MessageStruct.
 */
public class MessageStruct extends Structure {

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
		theEnum  = theEnum.getForValue (messageType);
		
		return theEnum;
	}
	
	/**
	 * Sets the message type enum.
	 *
	 * @param theEnum the new message type enum
	 */
	public void setMessageTypeEnum(MessageType theEnum) {
		messageType = theEnum.getIntValue();
	}
	
	
	/*
	public String toJson() {
		
		GsonBuilder gb = new GsonBuilder();
		gb.registerTypeAdapter(MessageStruct.class, new MessageStructSerialization());
		Gson gson = gb.create();
		
		String json = gson.toJson(this);
		
		return json;
	}
	
	 */

    
    
	
}
