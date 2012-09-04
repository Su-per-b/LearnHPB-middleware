package com.sri.straylight.fmuWrapper.voNative;

import com.sun.jna.Structure;

// TODO: Auto-generated Javadoc
/**
 * The Class TypeSpecEnumeration.
 */
public class TypeSpecEnumeration extends Structure {
	
	/**
	 * The Class ByReference.
	 */
	public static class ByReference extends TypeSpecEnumeration implements Structure.ByReference { }
	
	/** The min. */
	public int min;
	
	/** The max. */
	public int max;
	
	/** The min value status. */
	public int minValueStatus;
	
	/** The max value status. */
	public int maxValueStatus;


	/** The enumeration item ary. */
	public EnumerationItem.ByReference  enumerationItemAry;

	
	
}
