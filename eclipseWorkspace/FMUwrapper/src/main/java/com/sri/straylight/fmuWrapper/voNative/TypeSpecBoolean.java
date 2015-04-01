package com.sri.straylight.fmuWrapper.voNative;


import java.util.Arrays;
import java.util.List;

import com.sun.jna.Structure;

// TODO: Auto-generated Javadoc
/**
 * The Class TypeSpecBoolean.
 */
public class TypeSpecBoolean extends Structure {
	
	/**
	 * The Class ByReference.
	 */
	public static class ByReference extends TypeSpecBoolean implements Structure.ByReference { }
	
	public byte start;
	public byte fixed;

	public int startValueStatus;
	public int fixedValueStatus;

	public String declaredType;
	

	@Override
	protected List<String> getFieldOrder() {
	    return Arrays.asList(new String[] { "start", "fixed", "startValueStatus", "fixedValueStatus", "declaredType"});
	}
	
	
}
