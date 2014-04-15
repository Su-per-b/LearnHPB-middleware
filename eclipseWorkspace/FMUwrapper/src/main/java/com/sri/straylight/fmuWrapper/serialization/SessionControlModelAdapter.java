package com.sri.straylight.fmuWrapper.serialization;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.sri.straylight.fmuWrapper.voManaged.SessionControlAction;
import com.sri.straylight.fmuWrapper.voManaged.SessionControlModel;



public class SessionControlModelAdapter extends AdapterBase<SessionControlModel> {

	
	final protected String[][] fieldNamesEx_ = { 
			{ "value_", "v" }
		};
	
	
	public SessionControlModelAdapter() {
		super(SessionControlModel.class);
		
		super.init(fieldNamesEx_);
		
	}
	
	
//
	@Override
	public JsonElement serialize(SessionControlModel src, Type typeOfSrc,
			JsonSerializationContext context) {

		super.serialize(src, typeOfSrc, context);
		jsonObjectAdd_("action", src.getAction());
		

		return jsonObject_;

	}
	
	
	
	@Override
	public SessionControlModel deserialize(JsonElement jsonElement, Type typeOfT,
			JsonDeserializationContext context)

	throws JsonParseException {

	    super.deserialize(jsonElement, typeOfT, context);
	    
		//input
	    JsonElement jsonElement1 = jsonObject_.get("action");
	    
	    SessionControlAction sessionControlAction = deserializationContext_.deserialize
	    		(jsonElement1, SessionControlAction.class);
	    
	    
	    destObject_.setAction(sessionControlAction);
	    
	    
		return destObject_;

	}

}
