package com.sri.straylight.fmuWrapper.voNative;

import com.sun.jna.Structure;

public class EnumerationItem extends Structure {
	
	public static class ByReference extends EnumerationItem implements Structure.ByReference { }
	String name;
	String description;
	
}
