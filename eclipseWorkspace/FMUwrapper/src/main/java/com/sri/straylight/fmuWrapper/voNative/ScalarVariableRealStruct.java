package com.sri.straylight.fmuWrapper.voNative;



import java.util.Arrays;
import java.util.List;

import com.sun.jna.Structure;

// TODO: Auto-generated Javadoc
/**
 * The Class ScalarVariableRealStruct.
 */
public class ScalarVariableRealStruct extends ScalarVariableStructBase {
	
	
	public static class ByReference extends ScalarVariableRealStruct implements Structure.ByReference { }
	
	public TypeSpecReal.ByReference typeSpecReal;

	
	@Override
	protected List<String> getFieldOrder() {
	    return Arrays.asList(new String[] {  "name", "idx", "causality", "variability", "description", "valueReference", "typeSpecReal"});
	}
	
	
	
}
