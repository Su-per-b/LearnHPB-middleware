package com.sri.straylight.client.util;


import com.sri.straylight.fmuWrapper.voManaged.ScalarValueCollection;
import com.sri.straylight.fmuWrapper.voNative.ConfigStruct;
import com.sri.straylight.fmuWrapper.voNative.SimStateNative;

 
/**
 * The Interface IFmuConnect.
 */
public abstract class FmuConnectionAbstract {

	
	public abstract void setConfig(ConfigStruct configStruct);

	public abstract void requestStateChange(SimStateNative newState);

	public abstract void setScalarValueCollection(ScalarValueCollection collection);
	
	
}



