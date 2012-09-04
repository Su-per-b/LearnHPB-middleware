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

	/** The start. */
	public int start;
	
	/** The nominal. */
	public int nominal;
	
	/** The min. */
	public int min;
	
	/** The max. */
	public int max;
	
	/** The fixed. */
	public int fixed;
	
	/** The start value status. */
	public int startValueStatus;
	
	/** The nominal value status. */
	public int nominalValueStatus;
	
	/** The min value status. */
	public int minValueStatus;
	
	/** The max value status. */
	public int maxValueStatus;
	
	/** The fixed value status. */
	public int fixedValueStatus;

	/** The declared type. */
	public String declaredType;
}
