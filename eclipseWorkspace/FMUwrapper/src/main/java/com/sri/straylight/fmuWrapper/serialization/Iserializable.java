package com.sri.straylight.fmuWrapper.serialization;

public interface Iserializable {
	
//	public boolean serializeType = true;

	public String serialize();
	
	public boolean getSerializeType();

	public void setSerializeType(boolean serializeType);
	
	
}
