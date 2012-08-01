package com.sri.straylight.fmuWrapper.voNative;

import com.sun.jna.Structure;

public class ScalarVariableEnumerationStruct extends ScalarVariableStructBase {
	
	public static class ByReference extends ScalarVariableEnumerationStruct implements Structure.ByReference { }
	public TypeSpecEnumeration.ByReference typeSpecEnumeration;

	
	
}
