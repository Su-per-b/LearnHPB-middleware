package com.sri.straylight.fmuWrapper.serialization;



public class JsonSerializable implements Iserializable{


	public boolean serializeType = true;

	
	public String serialize() {
		return JsonController.getInstance().serialize(this);
	}
	
}
