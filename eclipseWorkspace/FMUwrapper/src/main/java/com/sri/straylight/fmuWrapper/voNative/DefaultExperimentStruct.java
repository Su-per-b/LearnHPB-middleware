package com.sri.straylight.fmuWrapper.voNative;

import java.util.Arrays;
import java.util.List;

import com.sri.straylight.fmuWrapper.serialization.Iserializable;
import com.sri.straylight.fmuWrapper.serialization.JsonController;
import com.sun.jna.Structure;

/**
 * The Class DefaultExperimentStruct.
 */
public class DefaultExperimentStruct
	extends Structure
	implements Iserializable {
	
	private boolean serializeType_ = true;
	
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
	
	
	public String serialize() {
		return JsonController.getInstance().serialize(this);
	}
	

	public boolean getSerializeType() {
		return serializeType_;
	}
	
	public void setSerializeType(boolean serializeType) {
		serializeType_ = serializeType;
	}
	
	
	@Override
	protected List<String> getFieldOrder() {
	    return Arrays.asList(new String[] { "startTime", "stopTime", "tolerance" });
	}
	
	

}
