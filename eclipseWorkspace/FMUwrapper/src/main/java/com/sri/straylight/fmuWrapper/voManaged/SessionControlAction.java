package com.sri.straylight.fmuWrapper.voManaged;

import com.sri.straylight.fmuWrapper.serialization.JsonController;
import com.sri.straylight.fmuWrapper.serialization.JsonSerializable;
import com.sri.straylight.fmuWrapper.voNative.JnaEnum;


public enum SessionControlAction implements JnaEnum<SessionControlAction>, JsonSerializable  {
	
	attachToSession,  // 0 - value is sessionID
	getInfo; // value is not used
	
	
	private static int start = 0;
	
	
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


	@Override
	public String toJsonString() {
		return JsonController.getInstance().toJsonString(this);
	}

	
}