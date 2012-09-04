package com.sri.straylight.fmuWrapper.voNative;





// TODO: Auto-generated Javadoc
/**
 * The Enum MessageType.
 */
public enum MessageType implements JnaEnum<MessageType>  {
	
	/** The message type_debug. */
	messageType_debug, 
	
	/** The message type_info. */
	messageType_info, 
	
	/** The message type_error. */
	messageType_error;
	     
	/** The start. */
	private static int start = 0;
	

	/* (non-Javadoc)
	 * @see com.sri.straylight.fmuWrapper.voNative.JnaEnum#getIntValue()
	 */
	public int getIntValue() {
	    return this.ordinal() + start;
	}
	
	
	/* (non-Javadoc)
	 * @see com.sri.straylight.fmuWrapper.voNative.JnaEnum#getForValue(int)
	 */
	public MessageType getForValue(int i) {
	    for (MessageType o : MessageType.values()) {
	        if (o.getIntValue() == i) {
	            return o;
	        }
	    }
	    return null;
	}

}

