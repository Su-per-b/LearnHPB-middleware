package com.sri.straylight.client.model;



import com.sri.straylight.fmuWrapper.voNative.MetaDataStruct;

public interface IFmuConnect {



	public void load();
	
	public void init();
	
	public void run();

	public void setMetaData(MetaDataStruct metaDataStruct);
	
	
}



