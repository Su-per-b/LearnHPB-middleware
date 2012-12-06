package com.sri.straylight.client.util;

import java.util.Vector;

import com.sri.straylight.fmuWrapper.Controller.FMUcontrollerGlobal;
import com.sri.straylight.fmuWrapper.Controller.ThreadedFMUcontroller;
import com.sri.straylight.fmuWrapper.voNative.ConfigStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarValueRealStruct;
import com.sri.straylight.fmuWrapper.voNative.SimStateNative;

// TODO: Auto-generated Javadoc
/**
 * The Class FmuConnectLocal.
 */
public class FmuConnectionLocal extends FmuConnectionAbstract {

	/** The fmu_. */
	private ThreadedFMUcontroller threadedFMUcontroller_;

	//constructor
	public FmuConnectionLocal() {
		
		FMUcontrollerGlobal fmuController = new FMUcontrollerGlobal(null);
		
		threadedFMUcontroller_ = new ThreadedFMUcontroller(fmuController);
		threadedFMUcontroller_.instantiateFMU();
	}
	
	
	public void setConfig(ConfigStruct configStruct) {
		threadedFMUcontroller_.setConfig(configStruct);
	}
	
	public void requestStateChange(SimStateNative simStateNative) {
		threadedFMUcontroller_.requestStateChange(simStateNative);
	}

	public void setScalarValues(Vector<ScalarValueRealStruct> scalarValueList) {
		threadedFMUcontroller_.setScalarValues(scalarValueList);
	}



}
