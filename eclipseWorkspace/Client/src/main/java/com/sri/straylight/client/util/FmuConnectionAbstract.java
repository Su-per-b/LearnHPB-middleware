package com.sri.straylight.client.util;


import java.util.Vector;

import com.sri.straylight.fmuWrapper.voManaged.ScalarValueCollection;
import com.sri.straylight.fmuWrapper.voNative.ConfigStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarValueRealStruct;
import com.sri.straylight.fmuWrapper.voNative.SimStateNative;

 
/**
 * The Interface IFmuConnect.
 */
public abstract class FmuConnectionAbstract {


//	public abstract void connect();
	
//	public abstract void xmlParse();
	
//	public abstract void run();

	public abstract void setConfig(ConfigStruct configStruct);

	public abstract void requestStateChange(SimStateNative newState);

	public abstract void setScalarValues(Vector<ScalarValueRealStruct> scalrValueList);

	public abstract void setScalarValues2(ScalarValueCollection collection);
	
	
}



