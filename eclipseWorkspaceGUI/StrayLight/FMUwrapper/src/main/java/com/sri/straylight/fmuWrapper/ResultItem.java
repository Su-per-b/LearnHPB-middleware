package com.sri.straylight.fmuWrapper;

public class ResultItem {
	
	public double time;
	public String string;
	public int primitiveCount;
	public ResultItemPrimitive[] primitiveAry;

	
	
	public ResultItem() {
		
	}
	
	
	public ResultItem(ResultItemStruct resultItemStruct) {
		
		this.time = resultItemStruct.time;
		this.string = resultItemStruct.string;
		this.primitiveCount = resultItemStruct.primitiveCount;
		
		ResultItemPrimitiveStruct[]  structAry = resultItemStruct.getPrimitives();
		
		int len = structAry.length;
		
		primitiveAry = new ResultItemPrimitive[len];
		
		for (int i = 0; i < len; i++) {
			primitiveAry[i] = new ResultItemPrimitive (structAry[i]);
		}
		
		
	}
	
	
	
	public String[] getStrings() {
		
		String[] strAry = new String[primitiveCount];
		for (int i = 0; i < primitiveCount; i++) {
			strAry[i] = primitiveAry[i].string;
		}
		
		return strAry;
	}
	
	
}
