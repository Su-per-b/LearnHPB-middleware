package com.sri.straylight.client.model;

import com.sri.straylight.fmuWrapper.voNative.ConfigStruct;

public class BaseModel {
	
	protected String title_;
	
	
	public BaseModel(String title) {
		title_ = title;
	}
	
	
	public String getTitle() {
		return title_;
	}


	public void update(ConfigStruct configStruct) {
		// TODO Auto-generated method stub
		
	}
	
	
}
