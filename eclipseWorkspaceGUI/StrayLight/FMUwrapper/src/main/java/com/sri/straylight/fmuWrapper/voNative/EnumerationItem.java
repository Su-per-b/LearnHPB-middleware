package com.sri.straylight.fmuWrapper.voNative;

import com.sun.jna.Structure;

// TODO: Auto-generated Javadoc
/**
 * The Class EnumerationItem.
 */
public class EnumerationItem extends Structure {
	
	/**
	 * The Class ByReference.
	 */
	public static class ByReference extends EnumerationItem implements Structure.ByReference { }
	
	/** The name. */
	String name;
	
	/** The description. */
	String description;
	
}
