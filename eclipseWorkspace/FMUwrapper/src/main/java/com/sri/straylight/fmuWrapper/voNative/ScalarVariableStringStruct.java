package com.sri.straylight.fmuWrapper.voNative;

import com.sun.jna.Structure;

// TODO: Auto-generated Javadoc
/**
 * The Class ScalarVariableStringStruct.
 */
public class ScalarVariableStringStruct extends ScalarVariableStructBase {

	/**
	 * The Class ByReference.
	 */
	public static class ByReference extends ScalarVariableStringStruct implements Structure.ByReference { }
	
	/** The type spec string. */
	public TypeSpecString.ByReference typeSpecString;
	
	
}
