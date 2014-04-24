package com.sri.straylight.fmuWrapper.event;

import com.sri.straylight.fmuWrapper.voNative.ConfigStruct;

// TODO: Auto-generated Javadoc
/**
 * The Class ConfigChangeRequest.
 */
public class ConfigChangeRequest extends SerializableEvent<ConfigStruct> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ConfigChangeRequest(Object source, ConfigStruct configStruct) {
        super(source, configStruct);
    }

}
