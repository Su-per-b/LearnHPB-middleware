package com.sri.straylight.fmuWrapper.voNative;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Structure;

// TODO: Auto-generated Javadoc
/**
 * The Class ScalarVariableEnumerationStruct.
 */
public class ScalarVariableEnumerationStruct extends ScalarVariableStructBase {
	

	public static class ByReference extends ScalarVariableEnumerationStruct implements Structure.ByReference { }
	
	public TypeSpecEnumeration.ByReference typeSpecEnumeration;


	@Override
	protected List<String> getFieldOrder() {
	    return Arrays.asList(new String[] {  "name", "idx", "causality", "variability", "description", "valueReference", "typeSpecEnumeration"});
	}
	
	
}
