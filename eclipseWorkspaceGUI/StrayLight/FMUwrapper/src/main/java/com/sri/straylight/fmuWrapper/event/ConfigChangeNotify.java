package com.sri.straylight.fmuWrapper.event;

import com.sri.straylight.fmuWrapper.voNative.ConfigStruct;

public class ConfigChangeNotify {
	
	public ConfigStruct payload;
	
	public ConfigChangeNotify(ConfigStruct configStruct) {
		payload = configStruct;
	}

	
}
