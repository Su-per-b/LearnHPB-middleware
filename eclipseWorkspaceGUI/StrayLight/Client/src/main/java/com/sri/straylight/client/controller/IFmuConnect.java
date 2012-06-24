package com.sri.straylight.client.controller;



import com.sri.straylight.fmuWrapper.voNative.ConfigStruct;

public interface IFmuConnect {


	public void connect();
	
	public void xmlParse();
	
	public void init();
	
	public void run();

	public void setConfig(ConfigStruct configStruct);

	public void stop();
	
	public void resume();

	public void changeInput(int idx, double value);


	
	
}



