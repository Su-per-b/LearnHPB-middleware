package com.sri.straylight.fmuWrapper.voNative;


import com.sun.jna.Structure;

// TODO: Auto-generated Javadoc
/**
 * The Class ScalarValueStruct.
 */
public class ScalarValueStruct extends Structure {
	
	/**
	 * The Class ByReference.
	 */
	public static class ByReference extends ScalarValueStruct implements Structure.ByReference { }
	
	/** The idx. */
	public int idx;
	
	/** The string. */
	public String string;
	  
}
