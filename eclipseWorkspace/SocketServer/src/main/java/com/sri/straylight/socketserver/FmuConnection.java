package com.sri.straylight.socketserver;

import java.util.Vector;

import com.sri.straylight.fmuWrapper.Controller.ThreadedFMUcontroller;
import com.sri.straylight.fmuWrapper.voNative.ConfigStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarValueRealStruct;
import com.sri.straylight.fmuWrapper.voNative.SimStateNative;

// TODO: Auto-generated Javadoc
/**
 * The Class FmuConnectLocal.
 */
public class FmuConnection  {

	/** The fmu_. */
	private ThreadedFMUcontroller threadedFMUcontroller_;

	//constructor
	public FmuConnection() {
		threadedFMUcontroller_ = new ThreadedFMUcontroller();
		threadedFMUcontroller_.instantiateFMU();
	}
	
	public void connect() {
		threadedFMUcontroller_.connect();
	}
	
	public void xmlParse() {
		threadedFMUcontroller_.xmlParse();
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

	public void run() {
		threadedFMUcontroller_.run();
	}

	public SimStateNative getSimStateNative() {
		return threadedFMUcontroller_.getSimStateNative();
	}

}
