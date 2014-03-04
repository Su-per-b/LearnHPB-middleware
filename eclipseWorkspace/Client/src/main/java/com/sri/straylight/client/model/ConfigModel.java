
package com.sri.straylight.client.model;

import com.sri.straylight.fmuWrapper.voNative.ConfigStruct;
import com.sri.straylight.fmuWrapper.voNative.SimStateNative;

public class ConfigModel extends BaseModel{

	
	protected ConfigStruct configStruct_;
	
	protected SimStateNative simStateNative_;
	
	public ConfigModel(String title) {
		super(title);
	}

	
	public void setStruct(ConfigStruct configStruct) {
		configStruct_ = configStruct;
		
	}
	
	public ConfigStruct getStruct() {
		return configStruct_;
		
	}


	public void setSimStateNative(SimStateNative simStateNative) {
		simStateNative_ = simStateNative;
	}
	
	public SimStateNative getSimStateNative() {
		return simStateNative_;
	}
	



}
