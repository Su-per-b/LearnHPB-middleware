package com.sri.straylight.fmuWrapper.serialization;


import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.sri.straylight.fmuWrapper.event.BaseEvent;


public class AdapterEventBase<T 
	extends BaseEvent<P>, P> extends AdapterBase<T> 
{
	
	protected P payload_;
	

	public JsonElement serialize(
			T src, 
			Type typeOfSrc,
			JsonSerializationContext context) {
		
		super.serialize(src, typeOfSrc, context);
		
		payload_ =  src.getPayload();
		JsonElement element = context_.serialize(payload_, payload_.getClass());
		
		jsonObject_.add("payload", element);
		
		return jsonObject_;
	}
	
	
    public void deserializeHelper_(
    		JsonElement jsonElement, 
    		Type typeOfT, 
    		JsonDeserializationContext context)
        throws JsonParseException {
    	
    	super.deserialize(jsonElement, typeOfT, context);
    	
        JsonElement jsonElement1 = jsonObject_.get("payload");
        payload_ = context.deserialize(jsonElement1, payload_.getClass());

    }

	

    


	
}


