package com.sri.straylight.fmuWrapper.voNative;

import com.sun.jna.Structure;

// TODO: Auto-generated Javadoc
/**
 * The Class TypeSpecReal.
 */
public class TypeSpecReal  extends Structure {
	
	/**
	 * The Class ByReference.
	 */
	public static class ByReference extends TypeSpecReal implements Structure.ByReference { }
	
	/** The start. */
	public double start;
	
	/** The nominal. */
	public double nominal;
	
	/** The min. */
	public double min;
	
	/** The max. */
	public double max;

	/** The start value status. */
	public int startValueStatus;
	
	/** The nominal value status. */
	public int nominalValueStatus;
	
	/** The min value status. */
	public int minValueStatus;
	
	/** The max value status. */
	public int maxValueStatus;

}
