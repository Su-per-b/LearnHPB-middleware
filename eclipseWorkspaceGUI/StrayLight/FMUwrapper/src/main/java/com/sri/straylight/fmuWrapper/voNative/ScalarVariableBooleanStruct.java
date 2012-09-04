package com.sri.straylight.fmuWrapper.voNative;


import com.sun.jna.Structure;

// TODO: Auto-generated Javadoc
/**
 * The Class ScalarVariableBooleanStruct.
 */
public class ScalarVariableBooleanStruct extends ScalarVariableStructBase {
	
	/**
	 * The Class ByReference.
	 */
	public static class ByReference extends ScalarVariableBooleanStruct implements Structure.ByReference { }
	
	/** The type spec boolean. */
	public TypeSpecBoolean.ByReference typeSpecBoolean;
	
}
