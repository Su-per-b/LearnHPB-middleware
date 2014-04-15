package com.sri.straylight.fmuWrapper.serialization;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.sri.straylight.fmuWrapper.voManaged.SessionControlAction;

public class SessionControlActionAdapter extends AdapterBase<SessionControlAction> {

	
	public SessionControlActionAdapter() {
		super(SessionControlAction.class);
	}
	
	
	@Override
	public JsonElement serialize(SessionControlAction src, Type typeOfSrc,
			JsonSerializationContext context) {

		super.serialize(src, typeOfSrc, context);
		
		int value =  src.getIntValue();
		JsonPrimitive primitive = new JsonPrimitive(value);
		jsonObject_.add("intValue", primitive);
		
		return jsonObject_;
	}
	
	
    @Override
    public SessionControlAction deserialize(
    		JsonElement jsonElement, 
    		Type typeOfT, 
    		JsonDeserializationContext context)
    
        throws JsonParseException {
    	
        destObject_ = SessionControlAction.attachToSession;
        super.deserializeHelper_(jsonElement, typeOfT, context);

		int intValue = jsonObject_.get("intValue").getAsInt();
        destObject_ = destObject_.getForValue(intValue);
        
        return destObject_;
    }
	
}






