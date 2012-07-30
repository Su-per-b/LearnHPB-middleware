package com.sri.straylight.fmuWrapper.event;

import com.sri.straylight.fmuWrapper.voNative.ConfigStruct;

public class ConfigChangeRequest {
	
	public ConfigStruct payload;
	
	public ConfigChangeRequest(ConfigStruct configStruct) {
		payload = configStruct;
	}

	
}
