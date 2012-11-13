package com.sri.straylight.fmuWrapper.event;

import com.sri.straylight.fmuWrapper.voNative.ConfigStruct;

// TODO: Auto-generated Javadoc
/**
 * The Class ConfigChangeNotify.
 */
public class ConfigChangeNotify extends BaseEvent<ConfigStruct> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ConfigChangeNotify(Object source, ConfigStruct configStruct) {
        super(source, configStruct);
    }
	
}
