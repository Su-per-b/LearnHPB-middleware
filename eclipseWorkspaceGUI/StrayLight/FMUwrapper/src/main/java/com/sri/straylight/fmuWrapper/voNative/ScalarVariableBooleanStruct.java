package com.sri.straylight.fmuWrapper.voNative;


import com.sun.jna.Structure;

public class ScalarVariableBooleanStruct extends ScalarVariableStructBase {
	
	public static class ByReference extends ScalarVariableBooleanStruct implements Structure.ByReference { }
	public TypeSpecBoolean.ByReference typeSpecBoolean;
	
}
