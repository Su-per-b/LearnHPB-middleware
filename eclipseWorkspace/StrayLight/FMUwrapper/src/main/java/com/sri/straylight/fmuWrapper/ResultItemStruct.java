package com.sri.straylight.fmuWrapper;



import com.sun.jna.Structure;

public class ResultItemStruct extends Structure {

	public double time;
	public String string;
	public int primitiveCount;
	public ResultItemPrimitiveStruct.ByReference primitive;

	
	public ResultItemPrimitiveStruct[] getPrimitives() {
		
		ResultItemPrimitiveStruct ps2 = this.primitive;
		ResultItemPrimitiveStruct[] ary = (ResultItemPrimitiveStruct[])ps2.toArray(primitiveCount);
		
		return ary;
	}
	
	public String[] getStrings() {
		
		String[] strAry = new String[primitiveCount];
		
		ResultItemPrimitiveStruct[] ary = getPrimitives();
		
		for (int i = 0; i < primitiveCount; i++) {
			
			ResultItemPrimitiveStruct primitiveStruct = ary[i];
			//String str =  primitiveStruct.string;
			
			strAry[i] = primitiveStruct.string;
			  
		}

		
		return strAry;
	}
	
}
