package com.sri.straylight.fmuWrapper.voNative;



import com.sun.jna.Structure;

public class ScalarValueStruct extends Structure {
	
	public static class ByReference extends ScalarValueStruct implements Structure.ByReference { }
	public int idx;
	public String string;
	  
	
}
