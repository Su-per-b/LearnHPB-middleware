package com.sri.straylight.fmuWrapper.serialization;

public interface Iserializable {
	
	public String serialize();
	
	public boolean getSerializeType();

	public void setSerializeType(boolean serializeType);
	
	
}
