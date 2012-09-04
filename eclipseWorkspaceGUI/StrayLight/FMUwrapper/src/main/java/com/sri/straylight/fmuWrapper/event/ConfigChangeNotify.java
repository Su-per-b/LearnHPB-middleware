package com.sri.straylight.fmuWrapper.event;

import com.sri.straylight.fmuWrapper.voNative.ConfigStruct;

// TODO: Auto-generated Javadoc
/**
 * The Class ConfigChangeNotify.
 */
public class ConfigChangeNotify {
	
	/** The payload. */
	public ConfigStruct payload;
	
	/**
	 * Instantiates a new config change notify.
	 *
	 * @param configStruct the config struct
	 */
	public ConfigChangeNotify(ConfigStruct configStruct) {
		payload = configStruct;
	}

	
}
