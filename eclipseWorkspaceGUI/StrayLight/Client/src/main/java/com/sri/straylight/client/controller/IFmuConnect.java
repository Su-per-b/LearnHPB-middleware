package com.sri.straylight.client.controller;



import java.util.Vector;

import com.sri.straylight.fmuWrapper.voNative.ConfigStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarValueRealStruct;
import com.sri.straylight.fmuWrapper.voNative.SimStateNative;

// TODO: Auto-generated Javadoc
/**
 * The Interface IFmuConnect.
 */
public interface IFmuConnect {


	/**
	 * Connect.
	 */
	public void connect();
	
	/**
	 * Xml parse.
	 */
	public void xmlParse();
	
	/**
	 * Inits the.
	 */
	public void init();
	
	/**
	 * Run.
	 */
	public void run();

	/**
	 * Sets the config.
	 *
	 * @param configStruct the new config
	 */
	public void setConfig(ConfigStruct configStruct);

	/**
	 * Change input.
	 *
	 * @param idx the idx
	 * @param value the value
	 */
	public void changeInput(int idx, double value);

	/**
	 * Request state change.
	 *
	 * @param newState the new state
	 */
	public void requestStateChange(SimStateNative newState);

	/**
	 * Change scalar values.
	 *
	 * @param scalrValueList the scalr value list
	 */
	public void changeScalarValues(Vector<ScalarValueRealStruct> scalrValueList);
	
	
}



