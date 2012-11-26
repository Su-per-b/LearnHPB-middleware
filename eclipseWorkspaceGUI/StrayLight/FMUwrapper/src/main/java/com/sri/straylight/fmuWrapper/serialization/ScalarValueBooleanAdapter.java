
package com.sri.straylight.fmuWrapper.serialization;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueBoolean;


/**
 * The Class ResultEventAdapter.
 */
public class ScalarValueBooleanAdapter 
	extends AdapterBase<ScalarValueBoolean> {

	final protected String[] fieldNames_ = { "value_" };

	
	public ScalarValueBooleanAdapter() {
		super();
		super.setFieldNames(fieldNames_);
	}
	
	@Override
	public JsonElement serialize(ScalarValueBoolean src, Type typeOfSrc,
			JsonSerializationContext context) {

		super.serialize(src, typeOfSrc, context);
		
		JsonPrimitive primitive = new JsonPrimitive(src.getIdx());
		jsonObject_.add("idx_", primitive);
		
		return jsonObject_;
	}
	

	@Override
	public ScalarValueBoolean deserialize(JsonElement jsonElement, Type typeOfT,
			JsonDeserializationContext context)

	throws JsonParseException {

		destObject_ = new ScalarValueBoolean();
		super.deserialize(jsonElement, typeOfT, context);
		
		
		int idx = jsonObject_.get("idx_").getAsInt();
		destObject_.setIdx(idx);
		
		
		
		return destObject_;

	}

}
