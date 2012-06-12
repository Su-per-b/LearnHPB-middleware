package com.sri.straylight.fmuWrapper.voNative;

import com.sun.jna.Structure;

public class DefaultExperimentStruct extends Structure {
	
	public static class ByReference extends DefaultExperimentStruct implements Structure.ByReference { }
	public double startTime;
	public double stopTime;
	public double tolerance;
	
}
