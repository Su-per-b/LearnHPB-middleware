package com.sri.straylight.fmuWrapper;

public class ResultItem {
	
	public double time;
	public String string;
	public int primitiveCount;
	public ResultItemPrimitiveStruct[] primitiveAry;

	
	
	public ResultItem() {
		
	}
	
	
	public ResultItem(ResultItemStruct resultItemStruct) {
		this.time = resultItemStruct.time;
		this.string = resultItemStruct.string;
		this.primitiveCount = resultItemStruct.primitiveCount;
		this.primitiveAry = resultItemStruct.getPrimitives();
		
	}
	
	
	
	public String[] getStrings() {
		
		String[] strAry = new String[primitiveCount];
		for (int i = 0; i < primitiveCount; i++) {
			ResultItemPrimitiveStruct primitiveStruct = primitiveAry[i];
			strAry[i] = primitiveStruct.string;
		}
		
		return strAry;
	}
	
	
}
