package com.sri.straylight.fmu;



import com.sun.jna.Structure;

public class ResultItemPrimitiveStruct extends Structure {
	
	public static class ByReference extends ResultItemPrimitiveStruct implements Structure.ByReference { }
	public int idx;
	public String string;
	  

}
