package com.sri.straylight.fmuWrapper.voManaged;

import com.sri.straylight.fmuWrapper.voNative.ScalarValueStruct;

public class ScalarValue {
	public int idx;
	public String string;
	
	
	public ScalarValue() {
		
	}
	
	
	public ScalarValue(ScalarValueStruct struct) {
		this.idx = struct.idx;
		this.string = struct.string;
	}
	
	
}
