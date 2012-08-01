package com.sri.straylight.fmuWrapper.voNative;

import com.sun.jna.Structure;

public class TypeSpecInteger extends Structure {
	
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

	public String declaredType;
}
