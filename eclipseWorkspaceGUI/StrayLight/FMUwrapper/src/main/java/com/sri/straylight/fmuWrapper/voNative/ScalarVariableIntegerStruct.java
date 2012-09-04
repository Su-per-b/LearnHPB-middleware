package com.sri.straylight.fmuWrapper.voNative;

import com.sun.jna.Structure;

// TODO: Auto-generated Javadoc
/**
 * The Class ScalarVariableIntegerStruct.
 */
public class ScalarVariableIntegerStruct extends ScalarVariableStructBase {
	
	/**
	 * The Class ByReference.
	 */
	public static class ByReference extends ScalarVariableIntegerStruct implements Structure.ByReference { }
	
	/** The type spec integer. */
	public TypeSpecInteger.ByReference typeSpecInteger;

	
}
