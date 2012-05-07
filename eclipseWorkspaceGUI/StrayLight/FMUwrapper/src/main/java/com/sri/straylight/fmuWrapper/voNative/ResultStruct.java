package com.sri.straylight.fmuWrapper.voNative;



import com.sun.jna.Structure;

public class ResultStruct extends Structure {

	public double time;
	public String string;
	public int scalarValueCount;
	public ScalarValueStruct.ByReference scalarValueStruct;

	
	public ScalarValueStruct[] getPrimitives() {

		if (this.scalarValueStruct == null) {
			return null;
		} else {
			ScalarValueStruct ps2 = this.scalarValueStruct;
			ScalarValueStruct[] ary;
			ary = (ScalarValueStruct[])ps2.toArray(scalarValueCount);
			return ary;
		}
	}
	
	public String[] getStrings() {
		
		String[] strAry = new String[scalarValueCount];
		
		ScalarValueStruct[] ary = getPrimitives();
		
		for (int i = 0; i < scalarValueCount; i++) {
			
			ScalarValueStruct primitiveStruct = ary[i];
			//String str =  primitiveStruct.string;
			
			strAry[i] = primitiveStruct.string;
			  
		}

		
		return strAry;
	}
	
}
