package com.sri.straylight.fmuWrapper.voNative;

import com.sri.straylight.fmuWrapper.serialization.JsonController;
import com.sri.straylight.fmuWrapper.serialization.JsonSerializable;
import com.sun.jna.Structure;

/**
 * The Class DefaultExperimentStruct.
 */
public class DefaultExperimentStruct
	extends Structure
	implements JsonSerializable {
	
	/**
	 * The Class ByReference.
	 */
	public static class ByReference extends DefaultExperimentStruct implements Structure.ByReference { }
	
	/** The start time. */
	public double startTime;
	
	/** The stop time. */
	public double stopTime;
	
	/** The tolerance. */
	public double tolerance;
	
	
	public String toJsonString() {
		return JsonController.getInstance().toJsonString(this);
	}
	
	
	
}
