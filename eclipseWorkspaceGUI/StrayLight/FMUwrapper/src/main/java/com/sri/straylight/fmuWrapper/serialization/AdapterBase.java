package com.sri.straylight.fmuWrapper.serialization;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class AdapterBase {
	
	
	protected JsonObject serializeHelper_ (Object src) {
		
    	JsonObject jsonObject = new JsonObject();
    	jsonObject.add("type", new JsonPrimitive(src.getClass().getCanonicalName()));
		
    	return jsonObject;
	}
	
}
