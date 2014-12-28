package com.sri.straylight.socketserver.model;

import com.sri.straylight.fmuWrapper.serialization.Iserializable;
import com.sri.straylight.fmuWrapper.voNative.JnaEnum;



public enum WebSocketConnectionState implements JnaEnum<WebSocketConnectionState>,  Iserializable {
	
	uninitialized,
	opened_new,
	closed,
	timed_out,
	dropped,
	reconnected,
	closeRequested;
	
	private static int start = 0;
	
	private boolean serializeType_ = true;
	
	
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


	@Override
	public String serialize() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean getSerializeType() {
		return serializeType_;
	}
	
	public void setSerializeType(boolean serializeType) {
		serializeType_ = serializeType;
	}
	
	
}