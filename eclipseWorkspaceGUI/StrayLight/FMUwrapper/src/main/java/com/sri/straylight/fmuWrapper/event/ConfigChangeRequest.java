package com.sri.straylight.fmuWrapper.event;

import com.sri.straylight.fmuWrapper.voNative.ConfigStruct;

// TODO: Auto-generated Javadoc
/**
 * The Class ConfigChangeRequest.
 */
public class ConfigChangeRequest {
	
	/** The payload. */
	public ConfigStruct payload;
	
	/**
	 * Instantiates a new config change request.
	 *
	 * @param configStruct the config struct
	 */
	public ConfigChangeRequest(ConfigStruct configStruct) {
		payload = configStruct;
	}

	
}
