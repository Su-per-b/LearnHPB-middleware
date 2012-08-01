package com.sri.straylight.client.controller;



import com.sri.straylight.fmuWrapper.voNative.ConfigStruct;
import com.sri.straylight.fmuWrapper.voNative.SimStateNative;

public interface IFmuConnect {


	public void connect();
	
	public void xmlParse();
	
	public void init();
	
	public void run();

	public void setConfig(ConfigStruct configStruct);

	public void changeInput(int idx, double value);

	public void requestStateChange(SimStateNative newState);
	
	
}



