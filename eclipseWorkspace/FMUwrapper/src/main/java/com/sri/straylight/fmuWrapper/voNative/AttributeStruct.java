package com.sri.straylight.fmuWrapper.voNative;

import com.sun.jna.Structure;



public class AttributeStruct extends Structure 
 {
	
	public static class ByReference extends AttributeStruct implements Structure.ByReference { }
	
	public String name;
	
	public String value;
	

}
