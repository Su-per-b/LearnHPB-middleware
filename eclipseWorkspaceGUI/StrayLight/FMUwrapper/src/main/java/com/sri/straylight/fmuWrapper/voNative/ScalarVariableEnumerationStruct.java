package com.sri.straylight.fmuWrapper.voNative;

import com.sun.jna.Structure;

// TODO: Auto-generated Javadoc
/**
 * The Class ScalarVariableEnumerationStruct.
 */
public class ScalarVariableEnumerationStruct extends ScalarVariableStructBase {
	
	/**
	 * The Class ByReference.
	 */
	public static class ByReference extends ScalarVariableEnumerationStruct implements Structure.ByReference { }
	
	/** The type spec enumeration. */
	public TypeSpecEnumeration.ByReference typeSpecEnumeration;

	
	
}
