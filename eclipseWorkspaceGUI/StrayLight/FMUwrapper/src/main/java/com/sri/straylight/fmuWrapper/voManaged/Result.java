package com.sri.straylight.fmuWrapper.voManaged;

import com.sri.straylight.fmuWrapper.voNative.ScalarValueStruct;
import com.sri.straylight.fmuWrapper.voNative.ResultStruct;

public class Result {
	
	public double time;
	public String string;
	public int scalarValueCount;
	public ScalarValue[] scalarValueAry;

	
	
	public Result() {
		
	}
	
	
	public Result(ResultStruct resultItemStruct) {
		
		this.time = resultItemStruct.time;
		this.string = resultItemStruct.string;
		this.scalarValueCount = resultItemStruct.scalarValueCount;
		
		ScalarValueStruct[]  structAry = resultItemStruct.getPrimitives();
		
		int len = structAry.length;
		
		scalarValueAry = new ScalarValue[len];
		
		for (int i = 0; i < len; i++) {
			scalarValueAry[i] = new ScalarValue (structAry[i]);
		}

	}
	
	
	
	public String[] getStrings() {
		
		String[] strAry = new String[scalarValueCount + 1];
		
		strAry[0] = Double.toString(time);
		
		for (int i = 0; i < scalarValueCount; i++) {
			strAry[i+1] = scalarValueAry[i].string;
		}
		
		return strAry;
	}
	
	
}
