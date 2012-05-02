package com.sri.straylight.fmuWrapper;

public class ResultItemPrimitive {
	public int idx;
	public String string;
	
	
	public ResultItemPrimitive() {
		
	}
	
	
	public ResultItemPrimitive(ResultItemPrimitiveStruct struct) {
		this.idx = struct.idx;
		this.string = struct.string;

	}
	
	
}
