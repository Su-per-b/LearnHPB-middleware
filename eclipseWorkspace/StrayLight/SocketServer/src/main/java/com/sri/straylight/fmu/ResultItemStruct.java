package com.sri.straylight.fmu;



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
	


	
}
