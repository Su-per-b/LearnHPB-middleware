package com.sri.straylight.socketserver.model;

import com.sri.straylight.fmuWrapper.voNative.JnaEnum;



public enum WebSocketConnectionState implements JnaEnum<WebSocketConnectionState> {
	
	uninitialized,
	opened_new,
	closed,
	timed_out,
	dropped,
	reconnected;
	
	private static int start = 0;
	
	
	public int getIntValue() {
	    return this.ordinal() + start;
	}

	
	public WebSocketConnectionState getForValue(int i) {
	    for (WebSocketConnectionState o : WebSocketConnectionState.values()) {
	        if (o.getIntValue() == i) {
	            return o;
	        }
	    }
	    return null;
	}

	
}