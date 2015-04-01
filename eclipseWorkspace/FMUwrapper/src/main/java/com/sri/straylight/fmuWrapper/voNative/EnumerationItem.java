package com.sri.straylight.fmuWrapper.voNative;

import java.util.Arrays;
import java.util.List;

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
	public String name;
	
	/** The description. */
	public String description;
	

	@Override
	protected List<String> getFieldOrder() {
	    return Arrays.asList(new String[] { "name", "description"});
	}
	
	
}
