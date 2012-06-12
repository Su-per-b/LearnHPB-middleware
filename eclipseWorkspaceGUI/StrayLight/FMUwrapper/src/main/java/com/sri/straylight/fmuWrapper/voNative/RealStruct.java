package com.sri.straylight.fmuWrapper.voNative;

import com.sun.jna.Structure;

public class RealStruct extends Structure {
	
	public static class ByReference extends RealStruct implements Structure.ByReference { }
	public double min;
	public double max;
	public double nominal;
	public double start;
	
}

	
