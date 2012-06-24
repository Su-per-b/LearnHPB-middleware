package com.sri.straylight.fmuWrapper.voNative;

import com.sun.jna.Structure;

public class ConfigStruct extends Structure {
	

	public DefaultExperimentStruct.ByReference defaultExperimentStruct;
	public double stepDelta;
}
