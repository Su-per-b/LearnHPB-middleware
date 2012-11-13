package com.sri.straylight.fmuWrapper.event;

import com.sri.straylight.fmuWrapper.voNative.ConfigStruct;

// TODO: Auto-generated Javadoc
/**
 * The Class ConfigChangeRequest.
 */
public class ConfigChangeRequest extends BaseEvent<ConfigStruct> {
	
	public ConfigChangeRequest(Object source, ConfigStruct configStruct) {
        super(source, configStruct);
    }

}
