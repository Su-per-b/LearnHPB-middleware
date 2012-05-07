package com.sri.straylight.fmuWrapper.voNative;





public enum MessageType implements JnaEnum<MessageType>  {
	
	messageType_debug, 
	messageType_info, 
	messageType_error;
	     
	private static int start = 0;
	

	public int getIntValue() {
	    return this.ordinal() + start;
	}
	
	
	public MessageType getForValue(int i) {
	    for (MessageType o : MessageType.values()) {
	        if (o.getIntValue() == i) {
	            return o;
	        }
	    }
	    return null;
	}

}

