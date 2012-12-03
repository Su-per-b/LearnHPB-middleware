package com.sri.straylight.fmuWrapper.serialization;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.sri.straylight.fmuWrapper.voNative.ConfigStruct;
import com.sri.straylight.fmuWrapper.voNative.DefaultExperimentStruct;
import com.sri.straylight.fmuWrapper.voNative.DefaultExperimentStruct.ByReference;



public class ConfigStructAdapter extends AdapterBase<ConfigStruct> {

	final protected String[] fieldNames_ = { "stepDelta" };

	public ConfigStructAdapter() {
		super();
		super.setFieldNames(fieldNames_);
	}

	@Override
	public JsonElement serialize(ConfigStruct src, Type typeOfSrc,
			JsonSerializationContext context) {

		super.serialize(src, typeOfSrc, context);

		DefaultExperimentStruct.ByReference struct = src.defaultExperimentStruct;

		JsonElement element = serializationContext_.serialize(struct,
				DefaultExperimentStruct.ByReference.class);
		jsonObject_.add("defaultExperimentStruct", element);

		return jsonObject_;

	}

	@Override
	public ConfigStruct deserialize(JsonElement jsonElement, Type typeOfT,
			JsonDeserializationContext context)

	throws JsonParseException {

		destObject_ = new ConfigStruct();
		super.deserialize(jsonElement, typeOfT, context);

		JsonElement je = jsonObject_.get("defaultExperimentStruct");
		
		DefaultExperimentStruct.ByReference struct = 
				context.deserialize(je, DefaultExperimentStruct.ByReference.class);

		destObject_.defaultExperimentStruct = struct;

		return destObject_;

	}

}
