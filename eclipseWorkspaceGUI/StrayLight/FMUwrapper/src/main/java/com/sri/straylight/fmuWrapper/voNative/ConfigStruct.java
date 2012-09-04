package com.sri.straylight.fmuWrapper.voNative;

import com.sun.jna.Structure;

// TODO: Auto-generated Javadoc
/**
 * The Class ConfigStruct.
 */
public class ConfigStruct extends Structure {
	

	/** The default experiment struct. */
	public DefaultExperimentStruct.ByReference defaultExperimentStruct;
	
	/** The step delta. */
	public double stepDelta;
}
