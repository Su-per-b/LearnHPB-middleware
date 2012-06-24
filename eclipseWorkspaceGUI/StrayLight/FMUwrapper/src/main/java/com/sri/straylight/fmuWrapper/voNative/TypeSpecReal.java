package com.sri.straylight.fmuWrapper.voNative;

import com.sun.jna.Structure;

public class TypeSpecReal  extends Structure {
	
	public static class ByReference extends TypeSpecReal implements Structure.ByReference { }
	public double start;
	public double nominal;
	public double min;
	public double max;

	public int startValueStatus;
	public int nominalValueStatus;
	public int minValueStatus;
	public int maxValueStatus;

}
