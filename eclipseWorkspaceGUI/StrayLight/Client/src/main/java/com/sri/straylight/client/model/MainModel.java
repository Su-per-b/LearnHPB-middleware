package com.sri.straylight.client.model;

import com.sri.straylight.client.ConnectTo;



public class MainModel {


	
	public IFmuConnect fmuConnect_;
	
	public ConnectTo connectTo = ConnectTo.connecTo_file;
	
	
	public MainModel() {
		
    	switch (connectTo) {
    	
		case connecTo_localhost :
			fmuConnect_ = new FmuConnectRemote("localhost");
			break;
		case connecTo_straylightsim_com :
			fmuConnect_ = new FmuConnectRemote("wintermute.straylightsim.com");
			break;
		case connecTo_file :
			fmuConnect_ = new FmuConnectLocal();
			break;
	}

	
		//fmuConnect_.addListener(this);
		//fmuConnect_.init();
		
	}
	
	
	
	
	
}
