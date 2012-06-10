package com.sri.straylight.SwingGUI.model;


import com.sri.straylight.fmuWrapper.event.FMUeventListener;

public interface IFmuConnect {


	public void init(FMUeventListener l);

	public void run();
	
	
}



