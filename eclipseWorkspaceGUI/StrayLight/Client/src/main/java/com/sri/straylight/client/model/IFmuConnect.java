package com.sri.straylight.client.model;


import com.sri.straylight.fmuWrapper.event.FMUeventListener;

public interface IFmuConnect {


	public void init(FMUeventListener l);

	public void init();
	
	public void run();
	
	
}



