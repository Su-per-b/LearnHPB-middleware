package com.sri.straylight.fmuWrapper.voNative;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Structure;



public class AttributeStruct extends Structure 
 {
	
	public static class ByReference extends AttributeStruct implements Structure.ByReference { }
	
	public String name;
	
	public String value;

	
	@Override
	protected List<String> getFieldOrder() {
	    return Arrays.asList(new String[] { "name", "value" });
	}

	
	
}
