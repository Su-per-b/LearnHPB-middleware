package com.sri.straylight.fmuWrapper.voManaged;

import com.sri.straylight.fmuWrapper.serialization.Iserializable;
import com.sri.straylight.fmuWrapper.serialization.JsonController;
import com.sri.straylight.fmuWrapper.voNative.JnaEnum;


public enum SessionControlAction implements JnaEnum<SessionControlAction>,  Iserializable   {
	
	attachToSession,  // 0 - value is sessionID
	getInfo; // value is not used
	
	
	private static int start = 0;
	
	private boolean serializeType_ = true;
	
	private static String toStringMap_[] = new String[] {
		"attachToSession",
		"getInfo"
	};
	
	
	public int getIntValue() {
		return this.ordinal() + start;
	}

	
	public SessionControlAction getForValue(int i) {
		for (SessionControlAction o : SessionControlAction.values()) {
			if (o.getIntValue() == i) {
				return o;
			}
		}
		return null;
	}
	
	
	public String toString() {
		int idx = getIntValue();
		
		return  toStringMap_[idx];
		
	}



	public String serialize() {
		return JsonController.getInstance().serialize(this);
	}
	
	
	public boolean getSerializeType() {
		return serializeType_;
	}
	
	public void setSerializeType(boolean serializeType) {
		serializeType_ = serializeType;
	}
	
}