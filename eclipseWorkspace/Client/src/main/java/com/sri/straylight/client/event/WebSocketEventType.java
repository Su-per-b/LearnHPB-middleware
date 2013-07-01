package com.sri.straylight.client.event;

import com.sri.straylight.fmuWrapper.voNative.JnaEnum;





// TODO: Auto-generated Javadoc
/**
 * The Enum MessageType.
 */
public enum WebSocketEventType implements JnaEnum<WebSocketEventType>  {
	
	/** The message type_debug. */
	webSocketEventType_debug, 
	
	/** The message type_info. */
	webSocketEventType_info, 
	
	/** The message type_error. */
	webSocketEventType_error;
	     
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
	public WebSocketEventType getForValue(int i) {
	    for (WebSocketEventType o : WebSocketEventType.values()) {
	        if (o.getIntValue() == i) {
	            return o;
	        }
	    }
	    return null;
	}

}

