package com.sri.straylight.fmuWrapper.voNative;

import com.sun.jna.Structure;

public class ScalarVariableIntegerStruct extends ScalarVariableStructBase {
	
	public static class ByReference extends ScalarVariableIntegerStruct implements Structure.ByReference { }
	public TypeSpecInteger.ByReference typeSpecInteger;

	
}
