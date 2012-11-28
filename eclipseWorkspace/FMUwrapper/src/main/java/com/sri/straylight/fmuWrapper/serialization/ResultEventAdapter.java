package com.sri.straylight.fmuWrapper.serialization;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.sri.straylight.fmuWrapper.event.ResultEvent;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueResults;

public class ResultEventAdapter extends
		AdapterEventBase<ResultEvent, ScalarValueResults> {

	
	@Override
	public ResultEvent deserialize(JsonElement jsonElement, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {

		super.deserializeHelper_(jsonElement, typeOfT, context);
		ResultEvent event = new ResultEvent(this, payload_);

		return event;

	}

}