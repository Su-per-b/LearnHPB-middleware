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
