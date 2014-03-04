package com.sri.straylight.client.model;

import com.sri.straylight.fmuWrapper.voNative.JnaEnum;





// TODO: Auto-generated Javadoc
/**
 * The Enum MessageType.
 */
public enum WebSocketState implements JnaEnum<WebSocketState>  {
	
	uninitialized, 
	open, 
	dropped,
	closed,
	error;
	
	     
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
	public WebSocketState getForValue(int i) {
	    for (WebSocketState o : WebSocketState.values()) {
	        if (o.getIntValue() == i) {
	            return o;
	        }
	    }
	    return null;
	}

}

