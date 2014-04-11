package com.sri.straylight.fmuWrapper.serialization;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.sri.straylight.fmuWrapper.event.ConfigChangeNotify;
import com.sri.straylight.fmuWrapper.event.ResultEvent;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueResults;
import com.sri.straylight.fmuWrapper.voNative.ConfigStruct;

public class ResultEventAdapter extends
		AdapterEventBase<ResultEvent, ScalarValueResults> {

	public ResultEventAdapter() {
		super(ResultEvent.class, ScalarValueResults.class);
	}
	
	@Override
	public ResultEvent deserialize(JsonElement jsonElement, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		
    	
    	ScalarValueResults payload = deserializePayload_(jsonElement, typeOfT, context);	
    	ResultEvent event = new ResultEvent(this, payload);
    
    	
		return event;

	}

}
