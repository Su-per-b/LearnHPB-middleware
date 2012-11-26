package com.sri.straylight.fmuWrapper.voNative;



import com.sun.jna.Structure;

// TODO: Auto-generated Javadoc
/**
 * The Class ScalarVariableRealStruct.
 */
public class ScalarVariableRealStruct extends ScalarVariableStructBase {
	
	/**
	 * The Class ByReference.
	 */
	public static class ByReference extends ScalarVariableRealStruct implements Structure.ByReference { }
	
	/** The type spec real. */
	public TypeSpecReal.ByReference typeSpecReal;


}
