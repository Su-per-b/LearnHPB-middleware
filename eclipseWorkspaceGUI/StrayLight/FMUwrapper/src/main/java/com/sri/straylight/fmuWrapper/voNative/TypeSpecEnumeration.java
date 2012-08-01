package com.sri.straylight.fmuWrapper.voNative;

import com.sun.jna.Structure;

public class TypeSpecEnumeration extends Structure {
	
	public static class ByReference extends TypeSpecEnumeration implements Structure.ByReference { }
	
	public int min;
	public int max;
	
	public int minValueStatus;
	public int maxValueStatus;


	public EnumerationItem.ByReference  enumerationItemAry;

	
	
}
