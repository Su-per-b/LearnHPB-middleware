package com.sri.straylight.SwingGUI;


import com.sri.straylight.fmuWrapper.event.FMUeventListener;

public interface IFmuConnect {

	
	public void init();

	public void run();
	
	public void addListener(FMUeventListener l);
	
	
	
}



