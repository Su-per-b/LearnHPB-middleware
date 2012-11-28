package com.sri.straylight.fmuWrapper.voNative;

import com.sun.jna.Structure;

// TODO: Auto-generated Javadoc
/**
 * The Class ScalarVariablesAllStruct.
 */
public class ScalarVariablesAllStruct extends Structure {
	
	public static class ByReference extends ScalarVariablesAllStruct implements Structure.ByReference { }
	
	public ScalarVariableCollectionStruct.ByReference input;
	public ScalarVariableCollectionStruct.ByReference output;
	public ScalarVariableCollectionStruct.ByReference internal;
	
}
