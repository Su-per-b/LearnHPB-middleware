package com.sri.straylight.fmuWrapper.serialization;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueReal;


/**
 * The Class ResultEventAdapter.
 */
public class ScalarValueRealAdapter extends AdapterBase<ScalarValueReal> {


	final protected String[][] fieldNamesEx_ = { 
			{ "idx_", "i" },
			{ "value_", "v" }
		};
	
	public ScalarValueRealAdapter() {
		

		super(ScalarValueReal.class);
		super.init(fieldNamesEx_);

	}

	@Override
	protected JsonElement init(ScalarValueReal src, Type typeOfSrc,
			JsonSerializationContext context) {

		sourceObject_ = src;
		typeOfT_ = typeOfSrc;
		serializationContext_ = context;
		
		jsonObject_ = new JsonObject();
		
		if (src.serializeType) {
			jsonObject_.add("t", new JsonPrimitive(typeString_));
		}


		return jsonObject_;
	}
	
	
	
}
