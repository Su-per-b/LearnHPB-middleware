package com.sri.straylight.fmuWrapper.serialization;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.sri.straylight.fmuWrapper.voNative.DefaultExperimentStruct.ByReference;
import com.sri.straylight.fmuWrapper.voNative.DefaultExperimentStruct;

public class DefaultExperimentStructAdapter
	extends AdapterBase<DefaultExperimentStruct.ByReference>  {
	
	
	final protected String[] fieldNames_ = {"startTime", "stopTime", "tolerance"};

	public DefaultExperimentStructAdapter() {
		super();
		super.setFieldNames(fieldNames_);
	}
	
	
	 @Override
	public JsonElement serialize(DefaultExperimentStruct.ByReference src, Type typeOfSrc,
			JsonSerializationContext context) {

			sourceObject_ = src;
			typeOfT_ = typeOfSrc;
			serializationContext_ = context;
			
			jsonObject_ = new JsonObject();
			
			jsonObject_.add("type", new JsonPrimitive(
					"com.sri.straylight.fmuWrapper.voNative.DefaultExperimentStruct$ByReference"
					));

		 
			if(fieldNames_.length > 0) {
				serializeFields(fieldNames_);
			}
			
			return jsonObject_;
			
		 

		

	}
	
    @Override
    public DefaultExperimentStruct.ByReference deserialize(
    		JsonElement jsonElement, 
    		Type typeOfT, 
    		JsonDeserializationContext context)
    
        throws JsonParseException {
    	
        destObject_ = new DefaultExperimentStruct.ByReference();
        
		double startTime = jsonObject_.get("startTime").getAsDouble();
		destObject_.startTime = startTime;
		
		double stopTime = jsonObject_.get("stopTime").getAsDouble();
		destObject_.stopTime = stopTime;
		
		double tolerance = jsonObject_.get("tolerance").getAsDouble();
		destObject_.tolerance = tolerance;
		
		
        return destObject_;

    }
    
}
