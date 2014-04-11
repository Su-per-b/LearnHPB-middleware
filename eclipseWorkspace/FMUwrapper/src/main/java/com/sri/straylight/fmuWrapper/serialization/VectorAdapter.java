package com.sri.straylight.fmuWrapper.serialization;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.sri.straylight.fmuWrapper.voManaged.SerializableVector;


public class VectorAdapter extends AdapterBase<SerializableVector<?>> {
	


	public VectorAdapter() {


	}

	
	@Override
	public JsonElement serialize(SerializableVector<?> src, Type typeOfSrc,
			JsonSerializationContext context) {

		super.serialize(src, typeOfSrc, context);
		

		
		return jsonObject_;
	}
	
	



	
	
}
