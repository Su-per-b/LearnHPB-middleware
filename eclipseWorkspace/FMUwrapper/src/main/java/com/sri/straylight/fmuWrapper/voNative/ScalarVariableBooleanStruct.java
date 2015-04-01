package com.sri.straylight.fmuWrapper.voNative;


import java.util.Arrays;
import java.util.List;

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
	
	
	@Override
	protected List<String> getFieldOrder() {
	    return Arrays.asList(new String[] {  "name", "idx", "causality", "variability", "description", "valueReference", "typeSpecBoolean"});
	}
	
}
