package com.sri.straylight.fmuWrapper.voNative;



import com.sun.jna.Structure;

public class ScalarVariableRealStruct extends ScalarVariableStructBase {
	
	public static class ByReference extends ScalarVariableRealStruct implements Structure.ByReference { }
	public TypeSpecReal.ByReference typeSpecReal;
	

	

}
