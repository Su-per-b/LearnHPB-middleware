package com.sri.straylight.fmuWrapper.serialization;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.sri.straylight.fmuWrapper.voNative.TypeSpecReal;

public class TypeSpecRealAdapter extends AdapterBase<TypeSpecReal> {
	
	final protected String[] fieldNames_ = { 
			"start",  "nominal", "min", "max", 
			"startValueStatus",  "nominalValueStatus", "minValueStatus", "maxValueStatus"
	};

	
	public TypeSpecRealAdapter() {
		super();
		super.setFieldNames(fieldNames_);
	}


	@Override
	public TypeSpecReal deserialize(JsonElement jsonElement, Type typeOfT,
			JsonDeserializationContext context)

	throws JsonParseException {

		destObject_ = new TypeSpecReal();
		super.deserialize(jsonElement, typeOfT, context);
		
		
		return destObject_;

	}
	
	
}
