package com.sri.straylight.fmuWrapper.voNative;

import com.sun.jna.Structure;

public class ScalarVariableStringStruct extends ScalarVariableStructBase {

	public static class ByReference extends ScalarVariableStringStruct implements Structure.ByReference { }
	public TypeSpecString.ByReference typeSpecString;
	
	
}
