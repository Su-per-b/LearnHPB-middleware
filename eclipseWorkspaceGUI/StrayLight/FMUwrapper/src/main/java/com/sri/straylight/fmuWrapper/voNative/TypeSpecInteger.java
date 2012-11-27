package com.sri.straylight.fmuWrapper.voNative;

import com.sun.jna.Structure;

// TODO: Auto-generated Javadoc
/**
 * The Class TypeSpecInteger.
 */
public class TypeSpecInteger extends Structure {
	
	/**
	 * The Class ByReference.
	 */
	public static class ByReference extends TypeSpecInteger implements Structure.ByReference { }


	public int start;
	public int nominal;
	public int min;
	public int max;
	public int fixed;
	
	public int startValueStatus;
	public int nominalValueStatus;
	public int minValueStatus;
	public int maxValueStatus;
	public int fixedValueStatus;

	/** The declared type. */
	public String declaredType;
}
