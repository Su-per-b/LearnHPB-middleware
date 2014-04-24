package com.sri.straylight.fmuWrapper.serialization;


import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.sri.straylight.fmuWrapper.event.SerializableEvent;



public class AdapterEventBase<T extends SerializableEvent<P>, P extends Iserializable> 
	extends AdapterBase<T> 
{
	
	protected P payload_;
	private Class<P> payloadClazz_;
	

	public AdapterEventBase(Class<T> clazz,
			Class<P> payloadClazz) {
		
		super(clazz);
		payloadClazz_ = payloadClazz;
	}



	public JsonElement serialize(
			T src, 
			Type typeOfSrc,
			JsonSerializationContext context) {
		
		super.serialize(src, typeOfSrc, context);
		
		payload_ =  src.getPayload();
		JsonElement element = serializationContext_.serialize(payload_, payload_.getClass());
		
		jsonObject_.add("payload", element);
		
		return jsonObject_;
	}
	
	

    
	protected P deserializePayload_(JsonElement jsonElement, Type typeOfT,
			JsonDeserializationContext context) {

		
		jsonElement_ = jsonElement;

		if (jsonElement_.isJsonObject()) {
			jsonObject_ = jsonElement_.getAsJsonObject();
		}

		deserializationContext_ = context;

        JsonElement jsonElement1 = jsonObject_.get("payload");
        P payload = context.deserialize(jsonElement1, payloadClazz_);
        
		return payload;
	}

	

    


	
}


