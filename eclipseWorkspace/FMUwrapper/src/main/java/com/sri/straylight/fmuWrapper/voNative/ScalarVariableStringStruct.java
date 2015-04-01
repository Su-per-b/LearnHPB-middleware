package com.sri.straylight.fmuWrapper.voNative;

import java.util.Arrays;
import java.util.List;

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
	

	@Override
	protected List<String> getFieldOrder() {
	    return Arrays.asList(new String[] {  "name", "idx", "causality", "variability", "description", "valueReference", "typeSpecString"});
	}

	
}
