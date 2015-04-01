package com.sri.straylight.fmuWrapper.voNative;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Structure;

// TODO: Auto-generated Javadoc
/**
 * The Class ScalarVariablesAllStruct.
 */
public class ScalarVariablesAllStruct extends Structure {
	
	
	public ScalarVariableCollectionStruct.ByReference input;
	public ScalarVariableCollectionStruct.ByReference output;
	public ScalarVariableCollectionStruct.ByReference internal;
	

	@Override
	protected List<String> getFieldOrder() {
	    return Arrays.asList(new String[] { "input", "output", "internal" });
	}
	
	
}
