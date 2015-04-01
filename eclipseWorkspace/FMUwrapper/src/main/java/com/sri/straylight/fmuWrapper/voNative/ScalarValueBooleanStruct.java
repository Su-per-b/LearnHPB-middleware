package com.sri.straylight.fmuWrapper.voNative;

import java.util.Arrays;
import java.util.List;

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
	
	@Override
	protected List<String> getFieldOrder() {
	    return Arrays.asList(new String[] { "idx", "value"});
	}
	
	
	
}
