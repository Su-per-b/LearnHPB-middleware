package com.sri.straylight.fmuWrapper.voNative;



import com.sun.jna.Structure;

public class ScalarValueRealStruct extends Structure {
	
//	public static class ByReference extends ScalarValueRealStruct implements Structure.ByReference { }
	public int idx;
	public double value;
	  
}
