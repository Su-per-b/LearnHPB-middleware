package com.sri.straylight.fmuWrapper.serialization;



public class JsonSerializable implements Iserializable {


	private boolean serializeType_ = true;

	
	public String serialize() {
		return JsonController.getInstance().serialize(this);
	}
	
	
	public boolean getSerializeType() {
		return serializeType_;
	}
	
	public void setSerializeType(boolean serializeType) {
		serializeType_ = serializeType;
	}
	
	
	
}
