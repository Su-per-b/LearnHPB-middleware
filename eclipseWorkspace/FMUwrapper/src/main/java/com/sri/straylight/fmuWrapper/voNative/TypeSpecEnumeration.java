package com.sri.straylight.fmuWrapper.voNative;

import java.util.Arrays;
import java.util.List;

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
	
	public int min;
	public int max;
	public int minValueStatus;
	public int maxValueStatus;

	/** The enumeration item ary. */
	public EnumerationItem.ByReference  enumerationItemAry;


	
	@Override
	protected List<String> getFieldOrder() {
	    return Arrays.asList(new String[] { "min", "max", "minValueStatus", "maxValueStatus", "enumerationItemAry"});
	}
	
	
}
