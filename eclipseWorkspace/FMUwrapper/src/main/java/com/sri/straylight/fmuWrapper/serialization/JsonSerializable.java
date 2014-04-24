package com.sri.straylight.fmuWrapper.serialization;



public class JsonSerializable implements Iserializable{


	public boolean serializeType = true;

	
	public String toJsonString() {
		return JsonController.getInstance().toJsonString(this);
	}
	
}
