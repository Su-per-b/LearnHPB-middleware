package com.sri.straylight.fmuWrapper.serialization;

import com.sri.straylight.fmuWrapper.voNative.DefaultExperimentStruct;

public class DefaultExperimentStructAdapter
	extends AdapterBase<DefaultExperimentStruct.ByReference>  {
	
	
	final protected String[] fieldNames_ = {"startTime", "stopTime", "tolerance"};
	
	
	public DefaultExperimentStructAdapter() {
		super(DefaultExperimentStruct.ByReference.class);
		super.init(fieldNames_);
	}
	
    
}
