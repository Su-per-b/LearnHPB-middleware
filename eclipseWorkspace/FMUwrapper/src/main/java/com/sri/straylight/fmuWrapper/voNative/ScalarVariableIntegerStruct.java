package com.sri.straylight.fmuWrapper.voNative;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Structure;

// TODO: Auto-generated Javadoc
/**
 * The Class ScalarVariableIntegerStruct.
 */
public class ScalarVariableIntegerStruct extends ScalarVariableStructBase {
	

	public static class ByReference extends ScalarVariableIntegerStruct implements Structure.ByReference { }
	
	public TypeSpecInteger.ByReference typeSpecInteger;


	@Override
	protected List<String> getFieldOrder() {
	    return Arrays.asList(new String[] {  "name", "idx", "causality", "variability", "description", "valueReference", "typeSpecInteger"});
	}
	
	
}
