package com.sri.straylight.fmuWrapper.voNative;

import com.sun.jna.Structure;

// TODO: Auto-generated Javadoc
/**
 * The Class ScalarValueBooleanStruct.
 */
public class ScalarValueBooleanStruct extends Structure{
	
	
	public static class ByReference extends ScalarValueBooleanStruct implements Structure.ByReference { }
	
	/** The idx. */
	public int idx;
	
	/** The value. */
	public boolean value;
	
}
